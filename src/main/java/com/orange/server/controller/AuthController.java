package com.orange.server.controller;

import com.orange.server.entity.User;
import com.orange.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || password == null) {
            response.put("code", 400);
            response.put("message", "用户名和密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> userOpt = userService.findByUsernameAndPassword(username, password);
        if (!userOpt.isPresent()) {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(401).body(response);
        }

        User user = userOpt.get();
        if (user.getStatus() == 0) {
            response.put("code", 403);
            response.put("message", "用户已被禁用");
            return ResponseEntity.status(403).body(response);
        }

        String token = "token-" + System.currentTimeMillis() + "-" + Math.random();

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        data.put("roleName", user.getRoleName());

        response.put("code", 200);
        response.put("message", "登录成功");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "退出成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();

        if (token == null || token.isEmpty()) {
            response.put("code", 401);
            response.put("message", "未授权");
            return ResponseEntity.status(401).body(response);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", "管理员");
        data.put("avatar", "");
        data.put("roles", new String[]{"ADMIN"});

        response.put("code", 200);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}