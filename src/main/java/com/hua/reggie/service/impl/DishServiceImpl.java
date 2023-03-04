package com.hua.reggie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.reggie.dto.DishDto;
import com.hua.reggie.entity.Dish;
import com.hua.reggie.entity.DishFlavor;
import com.hua.reggie.mapper.DishMapper;
import com.hua.reggie.service.DishFlavorService;
import com.hua.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {
    /**
     * 新增菜品
     * @param dishDto
     */
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());        //
        dishFlavorService.saveBatch(flavors);
    }
    @Override
    public DishDto getByIdWithFlavor(Long id){
        DishDto dishDto = new DishDto();
        Dish dish =this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        List<DishFlavor> flavors = dishFlavorService.list(Wrappers.lambdaQuery(DishFlavor.class)
                .eq(DishFlavor::getDishId,id));
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish
        this.updateById(dishDto);

        //删除原有的口味应的数据 通过菜品id
        dishFlavorService.remove(Wrappers.lambdaQuery(DishFlavor.class)
                .eq(DishFlavor::getDishId,dishDto.getId()));
        //添加当前提交的
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());        //
        dishFlavorService.saveBatch(flavors);
    }

}
