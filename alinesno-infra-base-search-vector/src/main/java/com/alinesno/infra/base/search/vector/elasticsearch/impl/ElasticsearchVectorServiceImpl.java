package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.KnnQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.elasticsearch.ElasticsearchHandle;
import com.alinesno.infra.base.search.vector.elasticsearch.EsConfiguration;
import com.alinesno.infra.base.search.vector.elasticsearch.HttpCode;
import com.alinesno.infra.base.search.vector.elasticsearch.exception.ExploException;
import com.alinesno.infra.base.search.vector.service.IElasticsearchVectorService;
import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                            .index(indexName + ".*")
                            .query(q -> q
                                    .match(t -> t
                                            .field(fileName)
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

    @SneakyThrows
    @Override
    public List<DocumentVectorBean> queryVectorDocument(String indexName, String queryText, int top) {

        String vecStr = "" ;
        List<Float> floats = JSON.parseArray(vecStr, Float.class);

        KnnQuery knnQuery = KnnQuery.of(m -> m.field("document_embedding").queryVector(floats).k(top).numCandidates(100));
        Query rangeQuery = RangeQuery.of(q -> q.field("id").lte(JsonData.of(100)))._toQuery();

        SearchResponse<DocumentVectorBean> search = client.search(s -> s
                        .index(indexName + ".*")
                        .query(rangeQuery)
                        .knn(knnQuery)
                , DocumentVectorBean.class);

        log.debug("knn Query = {}" , search);

        List<DocumentVectorBean> result = new ArrayList<>();
        for (Hit<DocumentVectorBean> hit : search.hits().hits()) {
            DocumentVectorBean source = hit.source();
            result.add(source);
        }

        log.info("SearchResponse: {}", JSON.toJSONString(result));

        return result ;
    }

    @SneakyThrows
    @Override
    public void createVectorIndex(String indexName) {

       String iName = generateIndexName(indexName) ;

        // 创建索引
        ElasticsearchIndicesClient indexClient = client.indices();
        boolean flag = indexClient.exists(req -> req.index(iName)).value();

        boolean result = false;

        // 目标索引已存在
        log.info("索引【" + iName + "】已存在！");
        if (!flag) {
            // 不存在则创建索引
            CreateIndexResponse response = indexClient.create(
                    req -> req.index(iName)
                            .mappings(m -> m.properties("document_embedding", p -> p.denseVector(v -> v.dims(1536).index(false))))
                            .mappings(m -> m.properties("id", p -> p.long_(l -> l.index(true))))
                            .mappings(m -> m.properties("document_content", p -> p.text(t -> t.analyzer("document_content"))))
                            .mappings(m -> m.properties("document_title", p -> p.text(t -> t.analyzer("document_title"))))
                            .mappings(m -> m.properties("document_desc", p -> p.text(t -> t.analyzer("document_desc"))))
                            .mappings(m -> m.properties("source_type", p -> p.text(t -> t.analyzer("source_type"))))
                            .mappings(m -> m.properties("source_url", p -> p.text(t -> t.analyzer("source_url"))))
                            .mappings(m -> m.properties("source_file", p -> p.text(t -> t.analyzer("source_file"))))
                            .mappings(m -> m.properties("token_size", p -> p.long_(l -> l.index(true))))
                            .mappings(m -> m.properties("author", p -> p.text(t-> t.analyzer("author"))))
                            .mappings(m -> m.properties("dataset_id", p -> p.long_(l -> l.index(true))))
                            .mappings(m -> m.properties("page", p -> p.long_(l -> l.index(true))))
                            .mappings(m -> m.properties("doc_chunk", p -> p.text(t -> t.analyzer("doc_chunk"))))
            ) ;
            result = Boolean.TRUE.equals(response.acknowledged());
            log.info("索引【" + iName + "】创建" + (result?"成功":"失败"));
        }
    }

    @SneakyThrows
    @Override
    public void insertVector(DocumentVectorBean documentVectorBean) {

        String indexName = documentVectorBean.getIndexName() ;
        createVectorIndex(indexName) ;

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
        // 更新索引内容
        String indexName = documentVectorBean.getIndexName() ;
        try {
            client.update(u -> u
                            .index(indexName)
                            .id(String.valueOf(documentVectorBean.getId()))
                            .doc(documentVectorBean)
                    , DocumentVectorBean.class
            );
        } catch (IOException e) {
            log.error("数据更新ES异常：{}", e.getMessage());
            throw new ExploException(HttpCode.ES_UPDATE_ERROR, "ES更新数据失败");
        }
    }

    @SneakyThrows
    @Override
    public void deleteVectorIndex(String indexName, long documentId) {
        boolean isDelete = client.indices().delete(req -> req.index("indexName")).acknowledged();
        log.info("删除索引结果:{}" , isDelete) ;
    }

    private DashScopeEmbeddingUtils getDashScopeEmbedding() {
        return new DashScopeEmbeddingUtils(esConfiguration.getApiKey());
    }

    /**
     * 生成索引名称
     *
     * @param indexBase  索引基础名称
     * @return 生成的索引名称
     */
    private String generateIndexName(String indexBase) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return indexBase + ("-" + LocalDate.now().format(formatter));
    }

}
