package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import cn.hutool.core.util.IdUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.base.search.api.IndexInfoDto;
import com.alinesno.infra.base.search.api.SearchRequestDto;
import com.alinesno.infra.base.search.vector.elasticsearch.HttpCode;
import com.alinesno.infra.base.search.vector.elasticsearch.exception.ExploException;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class ElasticsearchDocumentServiceImpl implements IElasticsearchDocumentService {

    @Autowired
    private ElasticsearchClient client;

    @Override
    public void createDocumentIndex(String indexBase, String indexType) {
        String indexName = StringUtils.isBlank(indexType)?indexBase : generateIndexName(indexBase, indexType) ;

        try {
            if(!hasIndex(indexName)){
                CreateIndexResponse indexResponse = client.indices().create(c -> c.index(indexName));
                log.debug("索引创建成功：{}", indexResponse);
            }
        } catch (IOException e) {
            log.error("索引创建失败：{}", e.getMessage());
            throw new ExploException(HttpCode.INDEX_CREATE_ERROR, "创建索引失败");
        }
    }

    @SneakyThrows
    @Override
    public List<IndexInfoDto> getIndexInfo(String indexName) {

//        if(!hasIndex(indexName)){
//            throw new ExploException(HttpCode.INDEX_NOT_EXISTS, "索引不存在");
//        }

        List<IndexInfoDto> result = new ArrayList<>() ;

        // 获取索引的统计数据
        IndicesStatsResponse statsResponse = client.indices().stats(s -> s.index(indexName+"*"));

        // 获取到数量存储量大小/文档数量/副本/分片数/索引类型
        Map<String, IndicesStats> stats = statsResponse.indices();

        for (Map.Entry<String, IndicesStats> entry : stats.entrySet()) {

            String indexName1 = entry.getKey();
            IndicesStats indexStats = entry.getValue();

            // 获取存储大小和文档数量
            long storageSize = 0;
            if (indexStats.total() != null && indexStats.total().store() != null) {
                storageSize = indexStats.total().store().totalDataSetSizeInBytes();
            }

            long docCount = 0;
            if (indexStats.total() != null && indexStats.total().docs() != null) {
                docCount = indexStats.total().docs().count();
            }

            // 获取分片数和副本数
            long shardNum = 0;
            if (indexStats.total() != null && indexStats.total().shardStats() != null) {
                shardNum = indexStats.total().shardStats().totalCount();
            }

            String healthStatus = null;
            if (indexStats.health() != null) {
                healthStatus = indexStats.health().jsonValue().toLowerCase();

                switch (healthStatus) {
                    case "green" -> log.info("索引健康状态为绿色，可以继续使用。");
                    case "yellow" -> log.warn("索引健康状态为黄色，可能存在部分数据丢失。");
                    case "red" -> log.error("索引健康状态为红色，存在严重问题，请尽快处理。");
                    default -> log.error("未知的索引健康状态：" + healthStatus);
                }
            }

            log.debug("索引名称: {}, 存储大小: {}, 文档数量: {}, 分片数: {}, 健康状态: {}" , indexName1, storageSize, docCount, shardNum, healthStatus);

            result.add(new IndexInfoDto(indexName1, storageSize, (int) docCount, (int) shardNum, healthStatus));
        }

        return result ;
    }

    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean hasIndex(String indexName) throws IOException {
        BooleanResponse exists = client.indices().exists(d -> d.index(indexName));
        return exists.value();
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
    @Override
    public String generateIndexName(String indexBase, String indexType) {
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
