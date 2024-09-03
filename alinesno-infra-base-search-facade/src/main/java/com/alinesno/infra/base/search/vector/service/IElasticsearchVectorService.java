package com.alinesno.infra.base.search.vector.service;


import com.alinesno.infra.base.search.vector.DocumentVectorBean;

import java.util.List;
import java.util.Map;

public interface IElasticsearchVectorService {

    /**
     * 通过文本查询索引内容
     * @param indexName
     * @param fileName
     * @param queryText
     * @param size
     * @return
     */
    List<DocumentVectorBean> queryDocument(String indexName, String fileName , String queryText , int size) ;

    /**
     * 通过文本查询向量库内容
     * @param indexName
     * @param queryText
     * @param size
     * @return
     */
    List<DocumentVectorBean> queryVectorDocument(String indexName, String queryText , int size) ;

    /**
     * 创建向量索引库
     * @param indexName
     */
    void createVectorIndex(String indexName);

    /**
     * 插入向量数据
     * @param documentVectorBean
     */
    void insertVector(DocumentVectorBean documentVectorBean);

    /**
     * 更新向量数据
     * @param documentVectorBean
     */
    void updateVector(DocumentVectorBean documentVectorBean) ;

    /**
     * 删除向量索引库
     * @param indexName
     */
    void deleteVectorIndex(String indexName, long documentId);

}
