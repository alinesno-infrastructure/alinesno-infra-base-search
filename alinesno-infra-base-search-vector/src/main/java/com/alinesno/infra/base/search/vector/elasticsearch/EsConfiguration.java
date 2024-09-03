package com.alinesno.infra.base.search.vector.elasticsearch;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
@Component
public class EsConfiguration {

    @Value("${alinesno.base.search.elasticsearch.host:127.0.0.1}")
    private String host;

    @Value("${alinesno.base.search.elasticsearch.port:9200}")
    private int port;

    @Value("${alinesno.base.search.elasticsearch.username:}")
    private String username;

    @Value("${alinesno.base.search.elasticsearch.password:}")
    private String password;

    @Value("${alinesno.base.search.dashscope.api-key:}")
    private String apiKey;

}