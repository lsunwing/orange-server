package com.orange.server.service;

import com.orange.server.entity.Role;
import com.orange.server.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> findAll() {
        return roleMapper.selectList(null);
    }

    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(roleMapper.selectById(id));
    }

    public List<Role> findByRoleName(String roleName) {
        return roleMapper.findByRoleNameContaining(roleName);
    }

    public List<Role> findByStatus(Integer status) {
        return roleMapper.findByStatus(status);
    }

    public Role save(Role role) {
        if (role.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.insert(role);
        } else {
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(role);
        }
        return role;
    }

    public void deleteById(Long id) {
        roleMapper.deleteById(id);
    }

    public Role update(Long id, Role role) {
        Role existing = roleMapper.selectById(id);
        if (existing != null) {
            existing.setRoleName(role.getRoleName());
            existing.setRoleCode(role.getRoleCode());
            existing.setStatus(role.getStatus());
            existing.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(existing);
            return existing;
        }
        return null;
    }
}