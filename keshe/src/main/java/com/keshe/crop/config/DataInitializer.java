package com.keshe.crop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 首次启动时导入演示数据。
 * schema.sql 由 Spring Boot 每次启动执行（CREATE TABLE IF NOT EXISTS），
 * 这里只在 crop 表为空时执行 data.sql，避免管理员修改数据后重启被重复插入。
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DataInitializer(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM crop", Integer.class);
        if (count != null && count > 0) {
            log.info("检测到已有 {} 条作物数据，跳过演示数据初始化。", count);
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource("data.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);
        log.info("演示数据初始化完成。");
    }
}
