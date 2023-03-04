package com.hua.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.reggie.entity.Employee;
import com.hua.reggie.service.EmployeeService;
import com.hua.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author hua
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-01-27 20:30:31
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




