package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alinesno.infra.base.search.vector.elasticsearch.ElasticsearchHandle;
import com.alinesno.infra.base.search.vector.elasticsearch.HttpCode;
import com.alinesno.infra.base.search.vector.elasticsearch.exception.ExploException;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ElasticsearchDocumentServiceImpl implements IElasticsearchDocumentService {

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private ElasticsearchHandle elasticsearchHandle ;

    @Override
    public void createDocumentIndex(String indexBase, String indexType) {
        String indexName = generateIndexName(indexBase, indexType) ;
        elasticsearchHandle.createIndex(indexName) ;
    }

    @Override
    public void saveJsonObject(String indexBase, String indexType, List<String> jsonArr) {
        String indexName = generateIndexName(indexBase, indexType) ;

        try {
            List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
            for (String json : jsonArr) {
                bulkOperationArrayList.add(BulkOperation.of(o -> o.index(i -> i.document(json))));
            }

            BulkResponse bulkResponse = client.bulk(b -> b.index(indexName).operations(bulkOperationArrayList));
            log.debug("bulkResponse = " + bulkResponse) ;
        } catch (IOException e) {
            log.error("数据插入ES异常：{}", e.getMessage());
            throw new ExploException(HttpCode.ES_INSERT_ERROR, "ES新增数据失败");
        }
    }

    @Override
    public List<Map<String, Object>> search(String indexName, String indexType, String queryText, int top) {
        return search(indexName, indexType, "content", queryText, top) ;
    }

    @Override
    public List<Map<String, Object>> search(String indexName, String indexType, String fieldName, String queryText , int top) {
        List<Map<String, Object>> documentParagraphs = Lists.newArrayList();
        try {
            SearchResponse<Map> search = client.search(s -> s
                            .index(indexName)
                            .query(q -> q
                                    .match(t -> t
                                            .field(fieldName)
                                            .query(queryText)
                                    ))
                            .from(0)
                            .size(top)
                    , Map.class
            );
            for (Hit<Map> hit : search.hits().hits()) {
                Map pd = hit.source();
                documentParagraphs.add(pd);
            }
        } catch (IOException e) {
            log.error("查询ES异常：{}", e.getMessage());
            throw new ExploException(HttpCode.ES_SEARCH_ERROR, "查询ES数据失败");
        }
        return documentParagraphs;
    }

    @Override
    public void deleteDocument(String indexName , Long documentId) {
        try {
            client.deleteByQuery(d -> d
                    .index(indexName)
                    .query(q -> q
                            .term(t -> t
                                    .field("id")
                                    .value(documentId)
                            )
                    )
            );
        } catch (IOException e) {
            log.error("查询ES异常：{}", e.getMessage());
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
