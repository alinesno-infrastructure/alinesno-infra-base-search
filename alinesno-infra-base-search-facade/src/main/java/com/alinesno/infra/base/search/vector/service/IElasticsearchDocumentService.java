package com.alinesno.infra.base.search.vector.service;

import java.util.List;
import java.util.Map;

/**
 * ElasticsearchDocumentService接口
 */
public interface IElasticsearchDocumentService {

    /**
     * 创建文档索引
     * @param indexBase
     * @param indexType
     */
    void createDocumentIndex(String indexBase , String indexType) ;

    /**
     * 保存JSON对象
     * @param indexBase
     * @param indexType
     */
    void saveJsonObject(String indexBase, String indexType, List<String> jsonArr);

    /**
     * 查询对象并返回List信息
     * @param indexName
     * @param indexType
     * @param queryText
     * @return
     */
    List<Map<String, Object>> search(String indexName, String indexType, String queryText, int top);

    /**
     * 精确查询对象字段并返回List信息
     * @param indexName
     * @param indexType
     * @param fieldName
     * @param queryText
     * @return
     */
    List<Map<String, Object>> search(String indexName, String indexType, String fieldName, String queryText, int top);

    /**
     * 删除索引
     * @param indexBase
     * @param documentId
     */
    void deleteDocument(String indexBase, Long documentId);
}
