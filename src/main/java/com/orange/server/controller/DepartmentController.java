package com.orange.server.controller;

import com.orange.server.entity.Department;
import com.orange.server.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDepartments(
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) Integer status) {
        
        Map<String, Object> response = new HashMap<>();
        List<Department> departments = departmentService.findAll();
        
        if (departmentName != null && !departmentName.isEmpty()) {
            departments = departments.stream()
                    .filter(d -> d.getDepartmentName().contains(departmentName))
                    .collect(Collectors.toList());
        }
        
        if (status != null) {
            departments = departments.stream()
                    .filter(d -> d.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        
        response.put("code", 200);
        response.put("data", departments);
        response.put("total", departments.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDepartmentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Department> department = departmentService.findById(id);
        
        if (!department.isPresent()) {
            response.put("code", 404);
            response.put("message", "部门不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("data", department.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDepartment(@RequestBody Department department) {
        Map<String, Object> response = new HashMap<>();
        
        if (department.getDepartmentName() == null || department.getDepartmentName().isEmpty()) {
            response.put("code", 400);
            response.put("message", "请填写部门名称");
            return ResponseEntity.badRequest().body(response);
        }
        
        Department savedDepartment = departmentService.save(department);
        
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", savedDepartment);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Map<String, Object> response = new HashMap<>();
        Department updatedDepartment = departmentService.update(id, department);
        
        if (updatedDepartment == null) {
            response.put("code", 404);
            response.put("message", "部门不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedDepartment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Department> department = departmentService.findById(id);
        if (!department.isPresent()) {
            response.put("code", 404);
            response.put("message", "部门不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        departmentService.deleteById(id);
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}