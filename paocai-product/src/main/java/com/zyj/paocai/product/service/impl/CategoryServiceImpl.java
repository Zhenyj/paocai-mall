package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.product.dao.CategoryDao;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        // 获取所有分类
        List<CategoryEntity> entities = this.list();

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
    public Long[] getCatelogPath(Long catelogId) {
        Long catId = catelogId;
        List<Long> list = new ArrayList<>(3);
        list.add(catelogId);
        while (true) {
            CategoryEntity entity = getById(catId);
            if (entity == null) {
                throw new RuntimeException("不存在分类id：" + catId + "的分类");
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

    /**
     * 递归获取子分类
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