package com.zyj.paocai.product.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.entity.vo.CatalogBaseVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.dao.CategoryDao;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.vo.Catalog2Vo;
import com.zyj.paocai.product.service.CategoryBrandRelationService;
import com.zyj.paocai.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private static final String CATEGORY_JSON = "categoryJson";
    private static final String CATEGORY_LIST_TREE = "categoryListTree";
    private static final long CATEGORY_LIST_TREE_EXPIRATION = 60 * 60 * 24 * 1L;
    private static final String CATEGORY_LIST_TREE_LOCK = "categoryListTree-lock";

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @SentinelResource(value = "listTreeCategory")
    @Override
    public List<CategoryEntity> listTree() throws BlockException {
        String categoryString = redisTemplate.opsForValue().get(CATEGORY_LIST_TREE);
        if (StringUtils.hasText(categoryString)) {
            List<CategoryEntity> list = JSON.parseObject(categoryString, new TypeReference<List<CategoryEntity>>() {
            });
            return list;
        }

        // ??????????????????????????????
        RLock lock = redissonClient.getLock(CATEGORY_LIST_TREE_LOCK);
        // ?????????????????????????????????????????????
        lock.lock(30L, TimeUnit.SECONDS);
        try {
            // ??????????????????
            List<CategoryEntity> list = getCategoryFromDb();
            redisTemplate.opsForValue().set(CATEGORY_LIST_TREE, JSON.toJSONString(list),
                    CATEGORY_LIST_TREE_EXPIRATION, TimeUnit.SECONDS);
            return list;
        } finally {
            // ??????????????????
            lock.unlock();
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private List<CategoryEntity> getCategoryFromDb() {
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String categoryString = redisTemplate.opsForValue().get(CATEGORY_LIST_TREE);
        if (StringUtils.hasText(categoryString)) {
            List<CategoryEntity> list = JSON.parseObject(categoryString, new TypeReference<List<CategoryEntity>>() {
            });
            return list;
        }
        log.info("???????????????????????????????????????????????????");
        List<CategoryEntity> entities = this.list();
        if (entities == null || entities.size() == 0) {
            throw new RRException(BizCodeEnum.PRODUCT_CATEGORY_EXCEPTION.getMsg(),
                    BizCodeEnum.PRODUCT_CATEGORY_EXCEPTION.getCode());
        }

        List<CategoryEntity> list = entities.stream().filter(entitiy -> {
            return entitiy.getParentCid() == 0;
        }).map(menu -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public Long[] getCatalogPath(Long catelogId) {
        Long catId = catelogId;
        List<Long> list = new ArrayList<>(3);
        list.add(catelogId);
        while (true) {
            CategoryEntity entity = getById(catId);
            if (entity == null) {
                throw new RRException("???????????????id???" + catId + "?????????", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
            }
            Long parentCid = entity.getParentCid();
            if (parentCid != null && parentCid == 0) {
                break;
            }
            list.add(parentCid);
            catId = parentCid;
        }
        Collections.reverse(list);
        return list.toArray(new Long[list.size()]);
    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
    }

    @Cacheable(value = "category", key = "#root.method.name")
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        String json = redisTemplate.opsForValue().get(CATEGORY_JSON);
        if (StringUtils.hasText(json)) {
            // ????????????
            Map<String, List<Catalog2Vo>> result = JSON.parseObject(json,
                    new TypeReference<Map<String, List<Catalog2Vo>>>() {
                    });
            return result;
        }
        // ??????????????????????????????
        log.info("???????????????????????????");
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // ????????????????????????
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);

        // ????????????
        Map<String, List<Catalog2Vo>> Catalog2VoMap = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // ??????????????????
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            // ????????????
            List<Catalog2Vo> Catalog2Vos = null;
            if (!CollectionUtils.isEmpty(categoryEntities)) {
                Catalog2Vos = categoryEntities.stream().map(l2 -> {
                    // ?????????????????????????????????????????????
                    List<CategoryEntity> categoryEntities1 = getParentCid(selectList, l2.getCatId());
                    List<Catalog2Vo.Catalog3Vo> catalog3Vos = null;
                    if (!CollectionUtils.isEmpty(categoryEntities1)) {
                        catalog3Vos = categoryEntities1.stream().map(l3 -> {
                            Catalog2Vo.Catalog3Vo Catalog3Vo = new Catalog2Vo.Catalog3Vo(l2.getCatId().toString(),
                                    l3.getCatId().toString(), l3.getName());
                            return Catalog3Vo;
                        }).collect(Collectors.toList());
                    } else {
                        log.info("????????????:{},??????????????????", l2.getCatId());
                    }

                    Catalog2Vo Catalog2Vo = new Catalog2Vo(v.getCatId().toString(), catalog3Vos,
                            l2.getCatId().toString(), l2.getName());
                    return Catalog2Vo;
                }).collect(Collectors.toList());
            }
            return Catalog2Vos;
        }));
        return Catalog2VoMap;
    }

    /**
     * ?????????????????????????????????
     *
     * @param category
     */
    @CacheEvict(value = "category", allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        // ??????????????????
        boolean b = updateById(category);
        if (b && StringUtils.hasText(category.getName())) {
            // ??????
            categoryBrandRelationService.updateCategory(category);
        }
    }

    /**
     * ??????????????????
     *
     * @param catalogId
     * @return
     */
    @Override
    public List<CatalogBaseVo> getCatalogBaseVoPath(Long catalogId) {
        Long catId = catalogId;
        List<CatalogBaseVo> list = new ArrayList<>(3);
        while (true) {
            CategoryEntity entity = getById(catId);
            if (entity == null) {
                throw new RRException("???????????????id???" + catId + "?????????", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
            }
            list.add(new CatalogBaseVo(entity.getCatId(), entity.getName()));
            Long parentCid = entity.getParentCid();
            if (parentCid != null && parentCid == 0) {
                break;
            }
            catId = parentCid;
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * ??????spuId??????????????????
     *
     * @param spuId
     * @return
     */
    @Override
    public CategoryEntity getCategoryBySpuId(Long spuId) {
        CategoryEntity category = categoryDao.getCategoryBySpuId(spuId);
        if (category == null) {
            throw new RRException("??????spu??????????????????,spuId:" + spuId, BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        return category;
    }

    /**
     * ??????????????????
     *
     * @param catalogId
     * @return
     */
    @Override
    public List<CategoryEntity> getSameLevelCategory(Long catalogId) {
        CategoryEntity category = getById(catalogId);
        if (category == null) {
            throw new RRException("??????????????????,??????id:" + catalogId, BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        List<CategoryEntity> list = getSameLevelCategoryByParentId(category.getParentCid());
        return list;
    }

    @Override
    public List<CategoryEntity> getSameLevelCategoryByParentId(Long parentId) {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", parentId));
    }

    /**
     * @param categoryEntities
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> getParentCid(List<CategoryEntity> categoryEntities, Long parentCid) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(item -> {
            return parentCid.equals(item.getParentCid());
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * ?????????????????????
     *
     * @param root
     * @param all
     * @return
     */
    public List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> list = all.stream().filter(entity -> {
            return root.getCatId().equals(entity.getParentCid());
        }).map(menu -> {
            if (menu.getCatLevel() < 3) {
                menu.setChildren(getChildren(menu, all));
            }
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return list;
    }

}