package com.orange.server.config;

import com.orange.server.entity.*;
import com.orange.server.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public void run(String... args) {
        initRoles();
        initDepartments();
        initUsers();
        initDataSources();
        initTasks();
    }

    private void initRoles() {
        if (roleMapper.selectCount(null) == 0) {
            String[][] roles = {
                {"管理员", "ADMIN"},
                {"普通用户", "USER"},
                {"审核员", "AUDITOR"},
                {"访客", "GUEST"}
            };
            
            for (String[] role : roles) {
                Role r = new Role();
                r.setRoleName(role[0]);
                r.setRoleCode(role[1]);
                r.setStatus(1);
                roleMapper.insert(r);
            }
        }
    }

    private void initDepartments() {
        if (departmentMapper.selectCount(null) == 0) {
            String[] departments = {"研发部", "运营部", "财务部", "市场部", "人事部"};
            
            for (String dept : departments) {
                Department d = new Department();
                d.setDepartmentName(dept);
                d.setStatus(1);
                departmentMapper.insert(d);
            }
        }
    }

    private void initUsers() {
        if (userMapper.selectCount(null) == 0) {
            String[][] users = {
                {"张三", "13800000001", "管理员", "研发部", "123456", "1"},
                {"李四", "13800000002", "普通用户", "运营部", "123456", "1"},
                {"王五", "13800000003", "审核员", "财务部", "123456", "0"},
                {"赵六", "13800000004", "普通用户", "研发部", "123456", "1"}
            };
            
            for (String[] user : users) {
                User u = new User();
                u.setUsername(user[0]);
                u.setPhone(user[1]);
                u.setRoleName(user[2]);
                u.setDepartmentName(user[3]);
                u.setPassword(user[4]);
                u.setStatus(Integer.parseInt(user[5]));
                userMapper.insert(u);
            }
        }
    }

    private void initDataSources() {
        if (dataSourceMapper.selectCount(null) == 0) {
            DataSource ds1 = new DataSource();
            ds1.setId(1001L);
            ds1.setDataName("省交建局报告源");
            ds1.setOwnerType("periodic");
            ds1.setTaskType("report");
            ds1.setStatus(1);
            dataSourceMapper.insert(ds1);
            
            DataSource ds2 = new DataSource();
            ds2.setId(1002L);
            ds2.setDataName("企查查失信核查源");
            ds2.setOwnerType("manual");
            ds2.setTaskType("warning");
            ds2.setStatus(1);
            dataSourceMapper.insert(ds2);
        }
    }

    private void initTasks() {
        if (taskMapper.selectCount(null) == 0) {
            Task t1 = new Task();
            t1.setTaskName("用户积分补偿");
            t1.setTaskType("manual");
            t1.setTaskCode("MANUAL_USER_POINT_COMPENSATE");
            t1.setDataSourceId(1001L);
            t1.setStatus("ready");
            t1.setLastRunTime("-");
            taskMapper.insert(t1);
            
            Task t2 = new Task();
            t2.setTaskName("销售日报汇总");
            t2.setTaskType("periodic");
            t2.setTaskCode("PERIODIC_SALE_DAILY_SUMMARY");
            t2.setPeriodicTaskType("report");
            t2.setDataId(1001L);
            t2.setCycleType("day");
            t2.setScheduleTime("09:00:00");
            t2.setStatus("running");
            t2.setLastRunTime("2026-03-21 09:00:00");
            taskMapper.insert(t2);
            
            Task t3 = new Task();
            t3.setTaskName("库存风险扫描");
            t3.setTaskType("periodic");
            t3.setTaskCode("PERIODIC_STOCK_RISK_SCAN");
            t3.setPeriodicTaskType("warning");
            t3.setDataId(1002L);
            t3.setCycleType("week");
            t3.setCycleValues("[1,3,5]");
            t3.setScheduleTime("22:30:00");
            t3.setStatus("paused");
            t3.setLastRunTime("2026-03-20 22:30:00");
            taskMapper.insert(t3);
        }
    }
}