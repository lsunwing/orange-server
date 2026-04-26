package com.orange.server.service;

import com.orange.server.entity.Department;
import com.orange.server.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> findAll() {
        return departmentMapper.selectList(null);
    }

    public Optional<Department> findById(Long id) {
        return Optional.ofNullable(departmentMapper.selectById(id));
    }

    public List<Department> findByDepartmentName(String departmentName) {
        return departmentMapper.findByDepartmentNameContaining(departmentName);
    }

    public List<Department> findByStatus(Integer status) {
        return departmentMapper.findByStatus(status);
    }

    public Department save(Department department) {
        if (department.getId() == null) {
            department.setCreateTime(LocalDateTime.now());
            department.setUpdateTime(LocalDateTime.now());
            departmentMapper.insert(department);
        } else {
            department.setUpdateTime(LocalDateTime.now());
            departmentMapper.updateById(department);
        }
        return department;
    }

    public void deleteById(Long id) {
        departmentMapper.deleteById(id);
    }

    public Department update(Long id, Department department) {
        Department existing = departmentMapper.selectById(id);
        if (existing != null) {
            existing.setDepartmentName(department.getDepartmentName());
            existing.setStatus(department.getStatus());
            existing.setUpdateTime(LocalDateTime.now());
            departmentMapper.updateById(existing);
            return existing;
        }
        return null;
    }
}