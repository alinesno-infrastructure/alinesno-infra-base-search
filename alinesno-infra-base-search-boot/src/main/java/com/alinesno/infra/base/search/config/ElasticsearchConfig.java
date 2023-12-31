package com.alinesno.infra.base.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearch配置
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${alinesno.base.search.elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${alinesno.base.search.elasticsearch.port}")
    private int elasticsearchPort;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, "http")));
    }
}