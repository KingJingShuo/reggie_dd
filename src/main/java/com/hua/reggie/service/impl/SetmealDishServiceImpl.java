package com.hua.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.reggie.entity.SetmealDish;
import com.hua.reggie.mapper.SetmealDishMapper;

import com.hua.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service("setmealDishService")
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

}
