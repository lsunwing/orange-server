package com.orange.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orange.server.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    
    List<Department> findByDepartmentNameContaining(@Param("departmentName") String departmentName);
    
    List<Department> findByStatus(@Param("status") Integer status);
}