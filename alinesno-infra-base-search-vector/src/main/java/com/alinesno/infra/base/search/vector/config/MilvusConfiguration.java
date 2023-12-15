package com.alinesno.infra.base.search.vector.config;

import io.milvus.client.MilvusServiceClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 此处功能代码参考 <a href="https://juejin.cn/post/7251501842986762301">SpringBoot整合Milvus</a>
 */
@Configuration
@Data
@Component
public class MilvusConfiguration {

    /**
     *  milvus ip addr
     */
    @Value("${alinesno.base.search.milvus.host}")
    private String ipAddr;

    /**
     * milvus   port
     */
    @Value("${alinesno.base.search.milvus.port}")
    private Integer port;

    @Bean
    @Scope("singleton")
    public MilvusServiceClient getMilvusClient() {
        return getMilvusFactory().getMilvusClient();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public MilvusRestClientFactory getMilvusFactory() {
        ipAddr = "192.168.101.18" ;
        port = 19530 ;
        return  MilvusRestClientFactory.build(ipAddr, port);
    }
}