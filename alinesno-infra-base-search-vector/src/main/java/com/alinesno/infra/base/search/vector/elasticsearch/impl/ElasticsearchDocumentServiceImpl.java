package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ElasticsearchDocumentServiceImpl implements IElasticsearchDocumentService {
    @Override
    public void createDocumentIndex(String indexBase, String indexType) {

    }

    @Override
    public void saveJsonObject(String indexBase, String indexType) {

    }

    @Override
    public List<Map<String, Object>> search(String indexBase, String indexType, String queryText) {
        return null;
    }

    @Override
    public List<Map<String, Object>> search(String indexBase, String indexType, String fieldName, String queryText) {
        return null;
    }

    @Override
    public void deleteDocument(String indexBase, Long documentId) {

    }

//    @Autowired
//    private RestClient restClient ;
//
//    @SneakyThrows
//    @Override
//    public void createDocumentIndex(String indexBase, String indexType) {
//        String indexName = generateIndexName(indexBase , indexType) ;
//
//        // 检测索引名称为books的是否存在
//        Request request = new Request("HEAD", "/" + indexName);
//        boolean indexExists = restClient.performRequest(request).getStatusLine().getStatusCode() == 200;
//        log.debug("Index '" + indexName + "' exists: " + indexExists);
//
//        // 如果索引不存在则创建索引
//        if (!indexExists) {
//
//            // json 使用XContentBuilder创建
//            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
//                    .startObject()
//                    .startObject("mappings")
//                    .startObject("properties")
//                    .startObject("id")
//                    .field("type", "long")
//                    .endObject()
//                    .endObject()  // properties
//                    .endObject()  // mappings
//                    .endObject();  // outermost object
//
//            String createIndexJson = Strings.toString(xContentBuilder) ; // xContentBuilder.string();
//            log.debug("createIndexJson = " + createIndexJson);
//
//            Request createIndexRequest = new Request("PUT", "/" +indexName);
//            createIndexRequest.setJsonEntity(createIndexJson);
//            restClient.performRequest(createIndexRequest);
//            log.debug("Created index '" + indexName + "'");
//        }
//
//    }
//
//    @SneakyThrows
//    @Override
//    public void saveJsonObject(String indexBase, String json) {
//
//        String indexName = generateIndexName(indexBase , "daily")  ;
//
//        Request insertRequest = new Request("POST", "/" + indexName + "/_doc");
//        insertRequest.setJsonEntity(json) ;
//        Response response = restClient.performRequest(insertRequest);
//        System.out.println("Document inserted with status code: " + response.getStatusLine().getStatusCode());
//
//    }
//
//    @Override
//    public List<Map<String, Object>> search(String indexBase, String indexType, String queryText) {
//        return search(indexBase, indexType, "content", queryText);
//    }
//
//    @Override
//    public List<Map<String, Object>> search(String indexBase, String indexType, String fieldName, String queryText) {
//
//        String indexName = generateIndexName(indexBase , indexType) ;
//
//        SearchRequest request = new SearchRequest.Builder()
//                .index("products")
//                .query(query._toQuery())
//                .build();
//
//
////        // 使用Jackson映射器创建传输层
////        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
////        ElasticsearchClient client = new ElasticsearchClient(transport);
////
////        // 分页查询es
////        List<Map<String, Object>> results = new ArrayList<>();
////
////        SearchResponse<Product> search = client.search(s -> s
////                        .index("products")
////                        .query(q -> q
////                                .term(t -> t
////                                        .field("name")
////                                        .value(v -> v.stringValue("testname"))
////                                )),
////                Product.class);
////
////        for (Hit<Product> hit: search.hits().hits()) {
////            processProduct(hit.source());
////        }
////
////        return results;
//
//        return null ;
//    }
//
//    @SneakyThrows
//    @Override
//    public void deleteDocument(String indexBase, Long documentId) {
//
//        String indexName = generateIndexName(indexBase , "daily")  ;
//        Request deleteRequest = new Request("DELETE", "/" + indexName + "/_doc/" + documentId);
//        Response response = restClient.performRequest(deleteRequest);
//        System.out.println("Document deleteRequest with status code: " + response.getStatusLine().getStatusCode());
//    }
//
//    /**
//     * 生成索引名称
//     *
//     * @param indexBase  索引基础名称
//     * @param indexType  索引类型
//     * @return 生成的索引名称
//     */
//    private String generateIndexName(String indexBase, String indexType) {
//        String indexName = indexBase;
//        DateTimeFormatter formatter;
//        if ("daily".equalsIgnoreCase(indexType)) {
//            formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//        } else if ("monthly".equalsIgnoreCase(indexType)) {
//            formatter = DateTimeFormatter.ofPattern("yyyy.MM");
//        } else {
//            throw new IllegalArgumentException("无效的索引类型。只支持 'daily' 和 'monthly'。");
//        }
//        indexName += "-" + LocalDate.now().format(formatter);
//        return indexName;
//    }

}
