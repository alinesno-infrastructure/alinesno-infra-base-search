package com.alinesno.infra.base.search.gateway.provider;


import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.alinesno.infra.common.facade.response.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文档控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/base/search/document")
public class DocumentSearchController {

    @Autowired
    private IElasticsearchDocumentService elasticsearchService ;

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
        elasticsearchService.saveJsonObject(indexBase , indexType) ;
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
        List<Map<String , Object>> data = elasticsearchService.search(indexBase , indexType , queryText)  ;
        return AjaxResult.success(data) ;
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
        List<Map<String , Object>> data = elasticsearchService.search(indexBase , indexType , fieldName , queryText)  ;
        return AjaxResult.success(data) ;
    }

    /**
     * 删除对象信息
     * @param indexBase
     * @param documentId
     * @return
     */
    @DeleteMapping("/delete")
    public AjaxResult deleteObject(@RequestParam String indexBase, @RequestParam Long documentId) {
        // TODO 实现删除逻辑
        elasticsearchService.deleteDocument(indexBase, documentId);
        return AjaxResult.success("删除成功");
    }

    /**
     * 更新对象信息
     * @param jsonObject
     * @param indexBase
     * @param indexType
     * @return
     */
    @PutMapping("/update")
    public AjaxResult updateObject(@RequestBody String jsonObject, @RequestParam String indexBase, @RequestParam String indexType) {
        // TODO 实现更新逻辑

        return AjaxResult.success("更新成功");
    }

    /**
     * 创建索引
     * @param indexName
     * @return
     */
    @PostMapping("/createIndex")
    public AjaxResult createIndex(@RequestParam String indexName) {
        String indexType = "day";
        elasticsearchService.createDocumentIndex(indexName , indexType);
        return AjaxResult.success("创建索引成功");
    }

}
