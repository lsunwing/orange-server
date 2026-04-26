package com.orange.server.service;

import com.orange.server.entity.User;
import com.orange.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return Optional.ofNullable(userMapper.findByUsernameAndPassword(username, password));
    }

    public List<User> findByUsername(String username) {
        return userMapper.findByUsernameContaining(username);
    }

    public List<User> findByRoleName(String roleName) {
        return userMapper.findByRoleNameContaining(roleName);
    }

    public List<User> findByDepartmentName(String departmentName) {
        return userMapper.findByDepartmentNameContaining(departmentName);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
        }
        return user;
    }

    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    public User update(Long id, User user) {
        User existing = userMapper.selectById(id);
        if (existing != null) {
            existing.setUsername(user.getUsername());
            existing.setPhone(user.getPhone());
            existing.setRoleName(user.getRoleName());
            existing.setDepartmentName(user.getDepartmentName());
            existing.setStatus(user.getStatus());
            existing.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(existing);
            return existing;
        }
        return null;
    }
}