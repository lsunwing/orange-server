package com.orange.server.service;

import com.orange.server.entity.Task;
import com.orange.server.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;

    public List<Task> findAll() {
        return taskMapper.selectList(null);
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskMapper.selectById(id));
    }

    public List<Task> findByTaskName(String taskName) {
        return taskMapper.findByTaskNameContaining(taskName);
    }

    public List<Task> findByTaskType(String taskType) {
        return taskMapper.findByTaskType(taskType);
    }

    public List<Task> findByPeriodicTaskType(String periodicTaskType) {
        return taskMapper.findByPeriodicTaskType(periodicTaskType);
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setCreateTime(LocalDateTime.now());
            if (task.getLastRunTime() == null) {
                task.setLastRunTime("-");
            }
            taskMapper.insert(task);
        } else {
            taskMapper.updateById(task);
        }
        return task;
    }

    public void deleteById(Long id) {
        taskMapper.deleteById(id);
    }

    public Task update(Long id, Task task) {
        Task existing = taskMapper.selectById(id);
        if (existing != null) {
            existing.setTaskName(task.getTaskName());
            existing.setTaskType(task.getTaskType());
            existing.setTaskCode(task.getTaskCode());
            existing.setDataSourceId(task.getDataSourceId());
            existing.setPeriodicTaskType(task.getPeriodicTaskType());
            existing.setDataId(task.getDataId());
            existing.setCycleType(task.getCycleType());
            existing.setCycleValues(task.getCycleValues());
            existing.setScheduleTime(task.getScheduleTime());
            existing.setStatus(task.getStatus());
            taskMapper.updateById(existing);
            return existing;
        }
        return null;
    }

    public Task start(Long id) {
        Task task = taskMapper.selectById(id);
        if (task != null) {
            task.setLastRunTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            taskMapper.updateById(task);
            return task;
        }
        return null;
    }

    public Task pause(Long id) {
        Task task = taskMapper.selectById(id);
        if (task != null) {
            task.setStatus("paused");
            taskMapper.updateById(task);
            return task;
        }
        return null;
    }

    public Task resume(Long id) {
        Task task = taskMapper.selectById(id);
        if (task != null) {
            task.setStatus("running");
            task.setLastRunTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            taskMapper.updateById(task);
            return task;
        }
        return null;
    }
}