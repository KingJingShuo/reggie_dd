package com.hua.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hua.reggie.common.R;
import com.hua.reggie.entity.Category;
import com.hua.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author : hua
 * @date : 2023/2/28 15:44
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page<Category>> page( @RequestParam int page,
                                   @RequestParam int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        categoryService.page(pageInfo, Wrappers.lambdaQuery(Category.class).orderByAsc(Category::getSort));
        return R.success(pageInfo);

    }



    @DeleteMapping
    public R<String> delete(@RequestParam Long id){
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }



    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }



    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
        @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categoryList = categoryService.list(Wrappers
                .lambdaQuery(Category.class)
                .eq(category.getType()!=null,Category::getType,category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime));
        return R.success(categoryList);
    }


//    @GetMapping("/list")
//    public R<List<Category>> list(@RequestParam int type){
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        List<Category> categoryList = categoryService.list(Wrappers
//                .lambdaQuery(Category.class)
//                .eq(Category::getType,type)
//                .orderByAsc(Category::getSort)
//                .orderByDesc(Category::getUpdateTime));
//        return R.success(categoryList);
//    }
}
