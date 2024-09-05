package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import cn.hutool.core.util.IdUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.base.search.api.SearchRequestDto;
import com.alinesno.infra.base.search.vector.elasticsearch.ElasticsearchHandle;
import com.alinesno.infra.base.search.vector.elasticsearch.HttpCode;
import com.alinesno.infra.base.search.vector.elasticsearch.exception.ExploException;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @SneakyThrows
    @Override
    public void saveJsonObject(String indexBase, String indexType, String jsonObject) {
        String indexName = generateIndexName(indexBase, indexType) ;

        Reader input = new StringReader(jsonObject) ;
        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index(indexName)
                .withJson(input)
        );

        IndexResponse response = client.index(request);
        log.info("Indexed with version " + response.version());
    }

    @Override
    public List<Map<String, Object>> searchByPage(SearchRequestDto dto) {
        dto.setFieldName("content");
        return searchFieldByPage(dto) ;
    }

    @Override
    public List<Map<String, Object>> searchFieldByPage(SearchRequestDto dto) {

        String indexName = dto.getIndexBase() ;
        String fieldName = dto.getFieldName() ;
        String queryText = dto.getQueryText() ;
        int currentPage = dto.getCurrentPage() ;
        int top = dto.getPageSize() ;

        List<Map<String, Object>> documentParagraphs = Lists.newArrayList();
        try {
            SearchResponse<Map> search = client.search(s -> s
                            .index(indexName+"*")
                            .query(q -> q
                                    .match(t -> t
                                            .field(fieldName)
                                            .query(queryText)
                                    ))
                            .from(currentPage)
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

    @SneakyThrows
    @Override
    public void saveBatchJsonObject(String indexBase, String indexType, List<String> objects) {

        BulkRequest.Builder br = new BulkRequest.Builder();
        String indexName = generateIndexName(indexBase , indexType) ;

        for (String doc : objects) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(IdUtil.getSnowflakeNextIdStr())
                            .document(JSONObject.parseObject(doc))
                    )
            );
        }

        BulkResponse result = client.bulk(br.build());
        log.debug("saveBatch = {}" , result);

        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
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
