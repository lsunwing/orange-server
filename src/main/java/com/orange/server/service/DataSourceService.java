package com.orange.server.service;

import com.orange.server.entity.DataSource;
import com.orange.server.mapper.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DataSourceService {
    @Autowired
    private DataSourceMapper dataSourceMapper;

    public List<DataSource> findAll() {
        return dataSourceMapper.selectList(null);
    }

    public Optional<DataSource> findById(Long id) {
        return Optional.ofNullable(dataSourceMapper.selectById(id));
    }

    public List<DataSource> findByDataName(String dataName) {
        return dataSourceMapper.findByDataNameContaining(dataName);
    }

    public List<DataSource> findByOwnerType(String ownerType) {
        return dataSourceMapper.findByOwnerType(ownerType);
    }

    public List<DataSource> findByTaskType(String taskType) {
        return dataSourceMapper.findByTaskType(taskType);
    }

    public DataSource save(DataSource dataSource) {
        if (dataSource.getId() == null) {
            dataSource.setCreateTime(LocalDateTime.now());
            dataSource.setUpdateTime(LocalDateTime.now());
            dataSourceMapper.insert(dataSource);
        } else {
            dataSource.setUpdateTime(LocalDateTime.now());
            dataSourceMapper.updateById(dataSource);
        }
        return dataSource;
    }

    public void deleteById(Long id) {
        dataSourceMapper.deleteById(id);
    }

    public DataSource update(Long id, DataSource dataSource) {
        DataSource existing = dataSourceMapper.selectById(id);
        if (existing != null) {
            existing.setDataName(dataSource.getDataName());
            existing.setOwnerType(dataSource.getOwnerType());
            existing.setTaskType(dataSource.getTaskType());
            existing.setStatus(dataSource.getStatus());
            existing.setUpdateTime(LocalDateTime.now());
            dataSourceMapper.updateById(existing);
            return existing;
        }
        return null;
    }
}