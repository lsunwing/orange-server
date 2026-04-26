package com.orange.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orange.server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    List<User> findByUsernameContaining(@Param("username") String username);
    
    List<User> findByRoleNameContaining(@Param("roleName") String roleName);
    
    List<User> findByDepartmentNameContaining(@Param("departmentName") String departmentName);
}