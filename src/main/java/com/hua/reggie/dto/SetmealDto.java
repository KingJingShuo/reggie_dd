package com.hua.reggie.dto;

import com.hua.reggie.entity.Setmeal;
import com.hua.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
