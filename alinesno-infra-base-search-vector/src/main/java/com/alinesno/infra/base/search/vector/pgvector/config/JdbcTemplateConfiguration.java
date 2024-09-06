package com.alinesno.infra.base.search.vector.pgvector.config;

import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
public class JdbcTemplateConfiguration {

    @Value("${alinesno.base.search.pgvector.jdbc-url:}")
    private String jdbcUrl;

    @Value("${alinesno.base.search.pgvector.username:}")
    private String username;

    @Value("${alinesno.base.search.pgvector.password:}")
    private String password;

    @Value("${alinesno.base.search.dashscope.api-key:}")
    private String apiKey; // 注意：这个API密钥看起来与数据库配置无关，你可能需要根据实际情况处理它

    // 获取JdbcTemplate Bean对象
    @Bean("pgvectorJdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        DataSource dataSource = configureDataSource();
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DashScopeEmbeddingUtils getDashScopeEmbedding() {
        return new DashScopeEmbeddingUtils(apiKey);
    }

    private DataSource configureDataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        try {
            // 加载PostgreSQL的JDBC驱动类
            Class.forName("org.postgresql.Driver");
            Driver driver = (Driver) Class.forName("org.postgresql.Driver").newInstance();
            dataSource.setDriver(driver);
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize data source", e);
        }
    }
}