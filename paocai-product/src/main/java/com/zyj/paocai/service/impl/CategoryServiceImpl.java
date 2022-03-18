package com.zyj.paocai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.dao.CategoryDao;
import com.zyj.paocai.entity.CategoryEntity;
import com.zyj.paocai.service.CategoryService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.utils.Query;
import org.springframework.stereotype.Service;

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

        List<CategoryEntity> collect = entities.stream().filter(entitiy -> {
            return entitiy.getCatId() == 0;
        }).map(menu -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return collect;
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