package com.alinesno.infra.base.search.vector.elasticsearch;

import com.alinesno.infra.base.search.vector.utils.EsClientBuilder;
import lombok.Data;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// TODO 待稳定位置
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

    @Bean
    private RestClient getRestClient() {
        return EsClientBuilder.getRestClientInstance(
                host ,
                port ,
                username,
                password);
    }

}