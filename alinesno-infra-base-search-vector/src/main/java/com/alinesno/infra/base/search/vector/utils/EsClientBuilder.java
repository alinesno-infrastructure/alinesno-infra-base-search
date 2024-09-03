package com.alinesno.infra.base.search.vector.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

@Slf4j
public class EsClientBuilder {

    private static RestClient restClient = null;

    public static RestClient getRestClientInstance(String hostname , int port , String username , String password) {
        if(restClient == null){
            restClient = getRestClient(hostname, port, username, password);
        }
        return restClient ;
    }

    // 关闭client
    public static void closeClient() {
        if (restClient != null) {
            try {
                restClient.close();
            } catch (Exception e) {
                log.error("关闭client失败:{}", e.getMessage());
            }
        }
    }

    private static RestClient getRestClient(String hostname , int port , String username , String password) {
        String scheme = "http";

        // 创建CredentialsProvider并设置认证信息
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password)
        );

        // 创建RestClientBuilder并设置HTTP客户端配置
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    return httpAsyncClientBuilder;
                });

        // 创建RestClient实例
        return builder.build();
    }
}
