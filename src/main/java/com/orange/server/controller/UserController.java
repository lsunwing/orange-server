package com.orange.server.controller;

import com.orange.server.entity.User;
import com.orange.server.service.UserService;
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
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        List<User> users = userService.findAll();
        
        if (username != null && !username.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getUsername().contains(username))
                    .collect(Collectors.toList());
        }
        
        if (roleName != null && !roleName.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getRoleName() != null && u.getRoleName().contains(roleName))
                    .collect(Collectors.toList());
        }
        
        if (departmentName != null && !departmentName.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getDepartmentName() != null && u.getDepartmentName().contains(departmentName))
                    .collect(Collectors.toList());
        }
        
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(startTime + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endTime + "T23:59:59");
            users = users.stream()
                    .filter(u -> u.getCreateTime() != null && 
                                !u.getCreateTime().isBefore(start) && 
                                !u.getCreateTime().isAfter(end))
                    .collect(Collectors.toList());
        }
        
        response.put("code", 200);
        response.put("data", users);
        response.put("total", users.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> user = userService.findById(id);
        
        if (!user.isPresent()) {
            response.put("code", 404);
            response.put("message", "用户不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("data", user.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        if (user.getUsername() == null || user.getPhone() == null || 
            user.getRoleName() == null || user.getDepartmentName() == null) {
            response.put("code", 400);
            response.put("message", "请填写完整的用户信息");
            return ResponseEntity.badRequest().body(response);
        }
        
        user.setPassword("123456");
        User savedUser = userService.save(user);
        
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", savedUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        User updatedUser = userService.update(id, user);
        
        if (updatedUser == null) {
            response.put("code", 404);
            response.put("message", "用户不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            response.put("code", 404);
            response.put("message", "用户不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        userService.deleteById(id);
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}