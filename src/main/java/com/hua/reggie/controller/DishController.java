package com.hua.reggie.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hua.reggie.common.R;
import com.hua.reggie.dto.DishDto;
import com.hua.reggie.entity.Category;
import com.hua.reggie.entity.Dish;
import com.hua.reggie.service.CategoryService;
import com.hua.reggie.service.DishFlavorService;
import com.hua.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : hua
 * @date : 2023/3/1 16:17
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorServiceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam int page,
                        @RequestParam int pageSize,
                        @RequestParam(required = false) String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        Page<Dish> dishPageInfo = new Page<>(page, pageSize);
        dishService.page(dishPageInfo, Wrappers.lambdaQuery(Dish.class)
                .like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByDesc(Dish::getCreateTime));
        Page<DishDto> dishDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "records");
        List<Dish> records = dishPageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPageInfo.setRecords(list);
        return R.success(dishDtoPageInfo);
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * 根据菜品id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto flavor = dishService.getByIdWithFlavor(id);
        return R.success(flavor);
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> id){
        log.info("确定{}",id.toString());
        dishService.removeByIds(id);
        return R.success("删除成功");
    }

    /**
     * 修改了前端的url 更新菜品状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status")
    public R<String> updateStatus(@RequestParam List<Long> id,@RequestParam int status) {
        List<Dish> dishList = dishService.listByIds(id);
        dishList.stream().peek((item)-> item.setStatus(status)).collect(Collectors.toList());
        dishService.updateBatchById(dishList);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        List<Dish> list = dishService.list(Wrappers.lambdaQuery(Dish.class)
                .eq(Dish::getCategoryId,dish.getCategoryId())
                .orderByDesc(Dish::getUpdateTime)
                .eq(Dish::getStatus,1));
        return R.success(list);
    }



 }
