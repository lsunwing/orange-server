package com.orange.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orange.server.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    List<Role> findByRoleNameContaining(@Param("roleName") String roleName);
    
    List<Role> findByStatus(@Param("status") Integer status);
}