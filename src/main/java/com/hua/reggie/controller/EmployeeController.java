package com.hua.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hua.reggie.common.R;
import com.hua.reggie.entity.Employee;
import com.hua.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author : hua
 * @date : 2023/1/27 20:37
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
       //1.
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.查数据库根据页面提交的用户名username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        //3.
        if(emp == null){
            return R.error("登陆失败");
        }
        //4.比对密码
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }
        //5.查看员工状态
        if(emp.getStatus()==0){
            return R.error("账号已被禁用!");
        }
        //TODO 将用户id放入 Session employee
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除Session中保存当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());
        //设置初始密码 md5加密

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录id
//       Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工");
    }

    /**
     *  员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> Page(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) String name
    ){
        log.info("page={},pageSize={},name={}" ,page,pageSize,name);
        //构造分页构造器
        Page<Employee> pageInfo  = new Page<>(page,pageSize);
        //添加条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo,queryWrapper);



        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        log.info(employee.toString());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工修改信息成功");
    }

    /**
     * 查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public  R<Employee> getById (@PathVariable Long id){
        log.info("查询员工信息...");
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }



}
