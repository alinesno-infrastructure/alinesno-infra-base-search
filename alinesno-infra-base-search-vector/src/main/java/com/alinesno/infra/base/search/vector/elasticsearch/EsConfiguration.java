package com.alinesno.infra.base.search.vector.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO 待稳定位置
@Configuration
@Data
@Component
public class EsConfiguration {

    @Value("${alinesno.base.search.elasticsearch.host:127.0.0.1:9200}")
    private String host;

    @Value("${alinesno.base.search.elasticsearch.username:}")
    private String username;

    @Value("${alinesno.base.search.elasticsearch.password:}")
    private String password;

    @Value("${alinesno.base.search.dashscope.api-key:}")
    private String apiKey;

    @Bean
    public ElasticsearchClient docqaElasticsearchClient() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        List<HttpHost> httpHosts = Lists.newArrayList();
        String[] split = host.split(",");
        for (int i = 0; i < split.length; i++) {
            httpHosts.add(HttpHost.create(split[i]));
        }
        HttpHost[] httpHosts1 = httpHosts.toArray(new HttpHost[0]);
        RestClient client = RestClient
                .builder(httpHosts1)
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setKeepAliveStrategy((response, context) -> 180 * 1000))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

//    @Bean
//    public RestClient getRestClient() {
//        return EsClientBuilder.getRestClientInstance(
//                host ,
//                port ,
//                username,
//                password);
//    }

}