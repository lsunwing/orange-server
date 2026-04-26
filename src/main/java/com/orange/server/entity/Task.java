package com.orange.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String taskName;

    private String taskType;

    private String taskCode;

    private Long dataSourceId;

    private String periodicTaskType;

    private Long dataId;

    private String cycleType;

    private String cycleValues;

    private String scheduleTime;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String lastRunTime;
}