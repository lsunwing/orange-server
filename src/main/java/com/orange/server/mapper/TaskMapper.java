package com.orange.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orange.server.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    
    List<Task> findByTaskNameContaining(@Param("taskName") String taskName);
    
    List<Task> findByTaskType(@Param("taskType") String taskType);
    
    List<Task> findByPeriodicTaskType(@Param("periodicTaskType") String periodicTaskType);
}