package com.alinesno.infra.base.search.vector.service;

import com.alinesno.infra.base.search.api.IndexInfoDto;
import com.alinesno.infra.base.search.api.SearchRequestDto;

import java.util.List;
import java.util.Map;

/**
 * ElasticsearchDocumentService接口
 */
public interface IElasticsearchDocumentService {

    /**
     * 生成索引名称
     *
     * @param indexBase  索引基础名称
     * @param indexType  索引类型
     * @return 生成的索引名称
     */
    public String generateIndexName(String indexBase, String indexType) ;

    /**
     * 创建文档索引
     * @param indexBase
     * @param indexType
     */
    void createDocumentIndex(String indexBase , String indexType) ;

    /**
     * 获取到索引当前信息
     */
    List<IndexInfoDto> getIndexInfo(String indexName);

    /**
     * 保存JSON对象
     * @param indexBase
     * @param indexType
     */
    void saveJsonObject(String indexBase, String indexType, String jsonObject);

    /**
     * 查询对象并返回List信息
     * @return
     */
    List<Map<String, Object>> searchByPage(SearchRequestDto dto);

    /**
     * 精确查询对象字段并返回List信息
     * @return
     */
    List<Map<String, Object>> searchFieldByPage(SearchRequestDto dto);

    /**
     * 删除索引
     * @param indexBase
     * @param documentId
     */
    void deleteDocument(String indexBase, Long documentId);

    /**
     * 保存多个对象文档到存储中
     * @param indexBase
     * @param indexType
     * @param objects
     */
    void saveBatchJsonObject(String indexBase, String indexType, List<String> objects);
}
