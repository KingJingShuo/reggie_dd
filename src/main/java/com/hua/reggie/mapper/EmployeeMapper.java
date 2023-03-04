package com.hua.reggie.mapper;

import com.hua.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hua
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2023-01-27 20:30:31
* @Entity com.hua.reggie.entity.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




