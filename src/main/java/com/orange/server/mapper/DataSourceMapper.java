package com.orange.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orange.server.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DataSourceMapper extends BaseMapper<DataSource> {
    
    List<DataSource> findByDataNameContaining(@Param("dataName") String dataName);
    
    List<DataSource> findByOwnerType(@Param("ownerType") String ownerType);
    
    List<DataSource> findByTaskType(@Param("taskType") String taskType);
}