package com.orange.server.controller;

import com.orange.server.entity.Role;
import com.orange.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        List<Role> roles = roleService.findAll();
        
        if (roleName != null && !roleName.isEmpty()) {
            roles = roles.stream()
                    .filter(r -> r.getRoleName().contains(roleName))
                    .collect(Collectors.toList());
        }
        
        if (status != null) {
            roles = roles.stream()
                    .filter(r -> r.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(startTime + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endTime + "T23:59:59");
            roles = roles.stream()
                    .filter(r -> r.getCreateTime() != null && 
                                !r.getCreateTime().isBefore(start) && 
                                !r.getCreateTime().isAfter(end))
                    .collect(Collectors.toList());
        }
        
        response.put("code", 200);
        response.put("data", roles);
        response.put("total", roles.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRoleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Role> role = roleService.findById(id);
        
        if (!role.isPresent()) {
            response.put("code", 404);
            response.put("message", "角色不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("data", role.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody Role role) {
        Map<String, Object> response = new HashMap<>();
        
        if (role.getRoleName() == null || role.getRoleCode() == null) {
            response.put("code", 400);
            response.put("message", "请填写完整的角色信息");
            return ResponseEntity.badRequest().body(response);
        }
        
        Role savedRole = roleService.save(role);
        
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", savedRole);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRole(@PathVariable Long id, @RequestBody Role role) {
        Map<String, Object> response = new HashMap<>();
        Role updatedRole = roleService.update(id, role);
        
        if (updatedRole == null) {
            response.put("code", 404);
            response.put("message", "角色不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedRole);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Role> role = roleService.findById(id);
        if (!role.isPresent()) {
            response.put("code", 404);
            response.put("message", "角色不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        roleService.deleteById(id);
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}