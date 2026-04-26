package com.orange.server.controller;

import com.orange.server.entity.Task;
import com.orange.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String taskType,
            @RequestParam(required = false) String periodicTaskType) {
        
        Map<String, Object> response = new HashMap<>();
        List<Task> tasks = taskService.findAll();
        
        if (taskName != null && !taskName.isEmpty()) {
            tasks = tasks.stream()
                    .filter(t -> t.getTaskName().contains(taskName))
                    .collect(Collectors.toList());
        }
        
        if (taskType != null && !taskType.isEmpty()) {
            tasks = tasks.stream()
                    .filter(t -> t.getTaskType().equals(taskType))
                    .collect(Collectors.toList());
        }
        
        if (periodicTaskType != null && !periodicTaskType.isEmpty()) {
            tasks = tasks.stream()
                    .filter(t -> t.getPeriodicTaskType() != null && t.getPeriodicTaskType().equals(periodicTaskType))
                    .collect(Collectors.toList());
        }
        
        response.put("code", 200);
        response.put("data", tasks);
        response.put("total", tasks.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Task> task = taskService.findById(id);
        
        if (!task.isPresent()) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("data", task.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(@RequestBody Task task) {
        Map<String, Object> response = new HashMap<>();
        
        if (task.getTaskName() == null || task.getTaskType() == null || task.getTaskCode() == null) {
            response.put("code", 400);
            response.put("message", "请填写完整的任务信息");
            return ResponseEntity.badRequest().body(response);
        }
        
        Task savedTask = taskService.save(task);
        
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", savedTask);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Map<String, Object> response = new HashMap<>();
        Task updatedTask = taskService.update(id, task);
        
        if (updatedTask == null) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedTask);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Task> task = taskService.findById(id);
        if (!task.isPresent()) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        taskService.deleteById(id);
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> startTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Task task = taskService.start(id);
        
        if (task == null) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "任务已启动");
        response.put("data", task);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<Map<String, Object>> pauseTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Task task = taskService.pause(id);
        
        if (task == null) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "任务已暂停");
        response.put("data", task);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<Map<String, Object>> resumeTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Task task = taskService.resume(id);
        
        if (task == null) {
            response.put("code", 404);
            response.put("message", "任务不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "任务已恢复");
        response.put("data", task);
        return ResponseEntity.ok(response);
    }
}