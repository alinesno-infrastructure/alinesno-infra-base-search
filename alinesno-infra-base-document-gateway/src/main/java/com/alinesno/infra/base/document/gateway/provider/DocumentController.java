package com.alinesno.infra.base.document.gateway.provider;


import com.alinesno.infra.common.facade.response.AjaxResult;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文档控制器
 */
@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 将数据保存到Elasticsearch
     *
     * @param jsonObject 要保存的JSON对象
     * @param indexBase  索引基础名称
     * @param indexType  索引类型
     * @return 保存结果信息
     */
    @PostMapping("/save")
    public AjaxResult saveToElasticSearch(@RequestBody String jsonObject, @RequestParam String indexBase, @RequestParam String indexType) {
        String indexName = generateIndexName(indexBase, indexType);
        IndexRequest request = new IndexRequest(indexName);
        request.source(jsonObject, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error("保存到Elasticsearch时发生错误");
        }
        return AjaxResult.success("保存到Elasticsearch成功");
    }

    /**
     * 根据查询文本进行搜索
     *
     * @param indexBase 索引基础名称
     * @param indexType 索引类型
     * @param queryText 查询文本
     * @return 搜索结果
     */
    @GetMapping("/search")
    public AjaxResult search(@RequestParam String indexBase, @RequestParam String indexType, @RequestParam String queryText) {
        String indexName = generateIndexName(indexBase, indexType);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("_all", queryText));
        searchRequest.source(searchSourceBuilder);
        try {
            return AjaxResult.success(client.search(searchRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("在Elasticsearch中搜索时发生错误", e);
        }
    }

    /**
     * 根据字段进行搜索
     *
     * @param indexBase 索引基础名称
     * @param indexType 索引类型
     * @param fieldName 字段名称
     * @param queryText 查询文本
     * @return 搜索结果
     */
    @GetMapping("/searchByField")
    public AjaxResult searchByField(@RequestParam String indexBase, @RequestParam String indexType, @RequestParam String fieldName, @RequestParam String queryText) {
        String indexName = generateIndexName(indexBase, indexType);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(fieldName, queryText));
        searchRequest.source(searchSourceBuilder);
        try {
            return AjaxResult.success(client.search(searchRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("在Elasticsearch中搜索时发生错误", e);
        }
    }

    /**
     * 生成索引名称
     *
     * @param indexBase  索引基础名称
     * @param indexType  索引类型
     * @return 生成的索引名称
     */
    private String generateIndexName(String indexBase, String indexType) {
        String indexName = indexBase;
        DateTimeFormatter formatter;
        if ("daily".equalsIgnoreCase(indexType)) {
            formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        } else if ("monthly".equalsIgnoreCase(indexType)) {
            formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        } else {
            throw new IllegalArgumentException("无效的索引类型。只支持 'daily' 和 'monthly'。");
        }
        indexName += "-" + LocalDate.now().format(formatter);
        return indexName;
    }
}
