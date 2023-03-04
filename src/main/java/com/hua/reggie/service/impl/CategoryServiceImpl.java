package com.hua.reggie.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.reggie.common.CustomException;
import com.hua.reggie.entity.Category;
import com.hua.reggie.entity.Dish;
import com.hua.reggie.entity.Setmeal;
import com.hua.reggie.mapper.CategoryMapper;
import com.hua.reggie.service.CategoryService;
import com.hua.reggie.service.DishService;
import com.hua.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category>
        implements  CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(@RequestParam Long id) {
        //添加查询条件
        int count1 =dishService.count(Wrappers.lambdaQuery(Dish.class).eq(Dish::getCategoryId,id));
        //查询当前分类 是否关联了菜品。如果已经关联，抛出一个异常
        if(count1>0){
            //已经关联，抛出异常类
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        //查询当前分类 是否关联了套餐。如果已经关联，抛出一个异常
        int count2=setmealService.count(Wrappers.lambdaQuery(Setmeal.class).eq(Setmeal::getCategoryId,id));
        if(count2>0){
            //已经关联套餐，抛出一个异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);

    }

}
