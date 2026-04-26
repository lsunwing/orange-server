package com.orange.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_data_source")
public class DataSource {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String dataName;

    private String ownerType;

    private String taskType;

    private Integer status = 1;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}