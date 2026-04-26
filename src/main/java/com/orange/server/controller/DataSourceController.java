package com.orange.server.controller;

import com.orange.server.entity.DataSource;
import com.orange.server.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/data-sources")
@CrossOrigin
public class DataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDataSources(
            @RequestParam(required = false) String dataName,
            @RequestParam(required = false) String ownerType,
            @RequestParam(required = false) String taskType) {
        
        Map<String, Object> response = new HashMap<>();
        List<DataSource> dataSources = dataSourceService.findAll();
        
        if (dataName != null && !dataName.isEmpty()) {
            dataSources = dataSources.stream()
                    .filter(d -> d.getDataName().contains(dataName))
                    .collect(Collectors.toList());
        }
        
        if (ownerType != null && !ownerType.isEmpty()) {
            dataSources = dataSources.stream()
                    .filter(d -> d.getOwnerType().equals(ownerType))
                    .collect(Collectors.toList());
        }
        
        if (taskType != null && !taskType.isEmpty()) {
            dataSources = dataSources.stream()
                    .filter(d -> d.getTaskType() != null && d.getTaskType().equals(taskType))
                    .collect(Collectors.toList());
        }
        
        response.put("code", 200);
        response.put("data", dataSources);
        response.put("total", dataSources.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDataSourceById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<DataSource> dataSource = dataSourceService.findById(id);
        
        if (!dataSource.isPresent()) {
            response.put("code", 404);
            response.put("message", "数据源不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("data", dataSource.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDataSource(@RequestBody DataSource dataSource) {
        Map<String, Object> response = new HashMap<>();
        
        if (dataSource.getDataName() == null || dataSource.getOwnerType() == null) {
            response.put("code", 400);
            response.put("message", "请填写完整的数据源信息");
            return ResponseEntity.badRequest().body(response);
        }
        
        DataSource savedDataSource = dataSourceService.save(dataSource);
        
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", savedDataSource);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDataSource(@PathVariable Long id, @RequestBody DataSource dataSource) {
        Map<String, Object> response = new HashMap<>();
        DataSource updatedDataSource = dataSourceService.update(id, dataSource);
        
        if (updatedDataSource == null) {
            response.put("code", 404);
            response.put("message", "数据源不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedDataSource);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDataSource(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<DataSource> dataSource = dataSourceService.findById(id);
        if (!dataSource.isPresent()) {
            response.put("code", 404);
            response.put("message", "数据源不存在");
            return ResponseEntity.status(404).body(response);
        }
        
        dataSourceService.deleteById(id);
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}