package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.elasticsearch.ElasticsearchHandle;
import com.alinesno.infra.base.search.vector.elasticsearch.EsConfiguration;
import com.alinesno.infra.base.search.vector.elasticsearch.HttpCode;
import com.alinesno.infra.base.search.vector.elasticsearch.exception.ExploException;
import com.alinesno.infra.base.search.vector.service.IElasticsearchVectorService;
import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ElasticsearchVectorServiceImpl implements IElasticsearchVectorService {

    @Autowired
    private EsConfiguration esConfiguration;

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private ElasticsearchHandle elasticsearchHandle ;

    @Override
    public List<DocumentVectorBean> queryDocument(String indexName, String fileName, String queryText, int top) {
        List<DocumentVectorBean> documentParagraphs = Lists.newArrayList();
        try {
            SearchResponse<DocumentVectorBean> search = client.search(s -> s
                            .index(indexName)
                            .query(q -> q
                                    .match(t -> t
                                            .field("content")
                                            .query(queryText)
                                    ))
                            .from(0)
                            .size(top)
                    , DocumentVectorBean.class
            );
            for (Hit<DocumentVectorBean> hit : search.hits().hits()) {
                DocumentVectorBean pd = hit.source();
                documentParagraphs.add(pd);
            }
        } catch (IOException e) {
            log.error("查询ES异常：{}", e.getMessage());
            throw new ExploException(HttpCode.ES_SEARCH_ERROR, "查询ES数据失败");
        }
        return documentParagraphs;
    }

    @Override
    public List<DocumentVectorBean> queryVectorDocument(String indexName, String queryText, int size) {
        return null;
    }

    @Override
    public void createVectorIndex(String indexName) {
        elasticsearchHandle.createIndex(indexName) ;
    }

    @Override
    public void insertVector(DocumentVectorBean documentVectorBean) {
        String indexName = documentVectorBean.getIndexName() ;

        try {
            List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
            bulkOperationArrayList.add(BulkOperation.of(o -> o.index(i -> i.document(documentVectorBean))));

            BulkResponse bulkResponse = client.bulk(b -> b.index(indexName).operations(bulkOperationArrayList));
            log.debug("bulkResponse = " + bulkResponse) ;
        } catch (IOException e) {
            log.error("数据插入ES异常：{}", e.getMessage());
            throw new ExploException(HttpCode.ES_INSERT_ERROR, "ES新增数据失败");
        }
    }

    @Override
    public void updateVector(DocumentVectorBean documentVectorBean) {

    }

    @Override
    public void deleteVectorIndex(String indexName, long documentId) {
        try {
            client.deleteByQuery(d -> d
                    .index(indexName)
                    .query(q -> q
                            .term(t -> t
                                    .field("docId")
                                    .value(documentId)
                            )
                    )
            );
        } catch (IOException e) {
            log.error("查询ES异常：{}", e.getMessage());
        }
    }

    private DashScopeEmbeddingUtils getDashScopeEmbedding() {
        return new DashScopeEmbeddingUtils(esConfiguration.getApiKey());
    }

}
