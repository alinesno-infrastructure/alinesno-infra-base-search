package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alinesno.infra.base.search.vector.elasticsearch.EsConfiguration;
import com.alinesno.infra.base.search.vector.service.IElasticsearchVectorService;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ElasticsearchVectorServiceImpl implements IElasticsearchVectorService {

    @Autowired
    private EsConfiguration esConfiguration;

    @Autowired
    private RestClient restClient ;

    @SneakyThrows
    @Override
    public List<DocumentVectorBean> queryDocument(String indexName, String fileName , String queryText, int size) {

        Request request = new Request("POST", "/" + indexName + "/_search");

        XContentBuilder queryBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("query")
                    .startObject("bool")
                        .startObject("filter")
                            .startObject("match")
                                .field(fileName, queryText)
                            .endObject()
                        .endObject()
                    .endObject()  // filter
                .endObject()  // bool
            .endObject();

        log.debug("queryBuilder = " + JSONUtil.toJsonPrettyStr(Strings.toString(queryBuilder)));

        request.setJsonEntity(Strings.toString(queryBuilder));
        Response response = restClient.performRequest(request);
        String responseString = EntityUtils.toString(response.getEntity()) ;
        log.debug("Query response: " + JSONUtil.toJsonPrettyStr(responseString) );

        return null;
    }

    @SneakyThrows
    @Override
    public List<DocumentVectorBean> queryVectorDocument(String indexName, String queryText, int size) {

        List<Double> embeddingVector = getDashScopeEmbedding().getEmbeddingDoubles(queryText);

        // 查询文档
        Request queryRequest = new Request("GET", "/" + indexName + "/_search");

//        XContentBuilder queryBuilder = XContentFactory.jsonBuilder().startObject()
//                    .startObject("query")
//                        .startObject("knn")
//                            .field("field", "document_embedding")
//                            .field("query_vector" , embeddingVector)
//                            .field("k", size)
//                            .field("num_candidates", 10)
//                        .endObject()  // knn
//                    .endObject()  // query
//                    .startArray("fields")
//                        .value("dataset_id")
//                        .value("indexName")
//                        .value("document_title")
//                        .value("document_content")
//                        .value("sourceFile")
//                        .value("sourceUrl")
//                        .value("sourceType")
//                        .value("author")
//                    .endArray()  // fields
//                .endObject();  // outermost object
				
        XContentBuilder queryBuilder = XContentFactory.jsonBuilder()
            .startObject() // 开始JSON对象
                .startArray("_source")
                    .value("dataset_id")
                    .value("indexName")
                    .value("document_title")
                    .value("document_content")
                    .value("sourceFile")
                    .value("sourceUrl")
                    .value("sourceType")
                    .value("author")
                .endArray()
                .startObject("query") // 开始查询对象
                    .startObject("script_score") // 开始script_score查询
                        .startObject("query")
                            .startObject("bool")
                                .startObject("filter")
                                    .startObject("term")
                                        .field("indexName", indexName)
                                    .endObject() // 结束term对象
                                .endObject() // 结束filter对象
                            .endObject() // 结束bool对象
                        .endObject() // 结束query对象
                        .startObject("script") // 开始script对象
                            .field("source", "cosineSimilarity(params.queryVector, doc['document_embedding']) + 1.0")
                            .startObject("params")
                                .field("queryVector" , embeddingVector)
                            .endObject() // 结束params对象
                        .endObject() // 结束script对象
                    .endObject() // 结束script_score查询
                .endObject()
            .endObject(); // 结束查询对象

        log.debug("queryBuilder = " + Strings.toString(queryBuilder));

        Response response = restClient.performRequest(queryRequest);
        String responseString = EntityUtils.toString(response.getEntity()) ;

        log.debug("Query response: " + responseString);

        JSONObject jsonObject = JSONUtil.parseObj(responseString) ;
        JSONArray responseArray =  jsonObject.getJSONObject("hits").getJSONArray("hits") ;

        if(responseArray != null){
            return responseArray.stream().map(item -> {

                DocumentVectorBean documentVectorBean = JSONUtil.toBean(JSONUtil.parseObj(item).getStr("_source"), DocumentVectorBean.class);
                documentVectorBean.setDocument_embedding(null);

                return documentVectorBean ;
            }).collect(Collectors.toList());
        }

        return null;
    }


    private DashScopeEmbeddingUtils getDashScopeEmbedding() {
        return new DashScopeEmbeddingUtils(esConfiguration.getApiKey());
    }



    @SneakyThrows
    @Override
    public void createVectorIndex(String indexName) {
        

        // 检测索引名称为books的是否存在
        Request request = new Request("HEAD", "/" + indexName);
        boolean indexExists = restClient.performRequest(request).getStatusLine().getStatusCode() == 200;
        log.debug("Index '" + indexName + "' exists: " + indexExists);

        // 如果不存在则自动创建索引
        if (!indexExists) {
                 XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("mappings")
                    .startObject("properties")
                        .startObject("id")
                            .field("type", "long")
                        .endObject()
                        .startObject("dataset_id")
                            .field("type", "long")
                        .endObject()
                        .startObject("indexName")
                            .field("type", "text")
                        .endObject()
                         .startObject("document_title")
                            .field("type", "text")
                        .endObject()
                        .startObject("document_content")
                            .field("type", "text")
                        .endObject()
                        .startObject("document_embedding")
                            .field("type", "dense_vector")
                            .field("dims", 1536)  // 根据实际向量维度调整
                            .field("index", false)
                        .endObject()
                        .startObject("score")
                            .field("type", "float")
                        .endObject()
                        .startObject("page")
                            .field("type", "integer")
                        .endObject()
                        .startObject("sourceFile")
                            .field("type", "text")
                        .endObject()
                        .startObject("sourceUrl")
                            .field("type", "text")
                        .endObject()
                        .startObject("sourceType")
                            .field("type", "text")
                        .endObject()
                        .startObject("author")
                            .field("type", "text")
                        .endObject()
                    .endObject()  // properties
                .endObject()  // mappings
            .endObject();  // outermost object

            String createIndexJson = Strings.toString(xContentBuilder) ; // xContentBuilder.string();
            log.debug("createIndexJson = " + createIndexJson);

            request = new Request("PUT", "/" + indexName);
            request.setJsonEntity(createIndexJson);
            restClient.performRequest(request);
            log.debug("Created index '" + indexName + "'");
        }
    }

    @SneakyThrows
    @Override
    public void insertVector(DocumentVectorBean documentVectorBean) {
        
        String indexName = documentVectorBean.getIndexName() ;

        List<Double> embeddingVector = getDashScopeEmbedding().getEmbeddingDoubles(documentVectorBean.getDocument_content());

        // 构建文档内容
        XContentBuilder document = XContentFactory.jsonBuilder().startObject()
                    .field("id", documentVectorBean.getId())
                    .field("dataset_id", documentVectorBean.getDataset_id())
                    .field("indexName", documentVectorBean.getIndexName())
                    .field("document_title", documentVectorBean.getDocument_title())
                    .field("document_content", documentVectorBean.getDocument_content())
                    .field("document_embedding", embeddingVector)
                    .field("score", documentVectorBean.getScore())
                    .field("page", documentVectorBean.getPage())
                    .field("sourceFile", documentVectorBean.getSourceFile())
                    .field("sourceUrl", documentVectorBean.getSourceUrl())
                    .field("sourceType", documentVectorBean.getSourceType())
                    .field("author", documentVectorBean.getAuthor())
                .endObject();

        log.debug("document = " + Strings.toString(document));

        // 将文档插入到Elasticsearch
        Request insertRequest = new Request("POST", "/" + indexName + "/_doc");
        insertRequest.setJsonEntity(Strings.toString(document));
        Response response = restClient.performRequest(insertRequest);
        log.debug("Document inserted with status code: " + response.getStatusLine().getStatusCode());
    }



    @SneakyThrows
    @Override
    public void updateVector(DocumentVectorBean documentVectorBean) {
        // 更新文档内容
        
        String indexName = documentVectorBean.getIndexName() ;

        XContentBuilder document = XContentFactory.jsonBuilder().startObject()
                .field("id", documentVectorBean.getId())
                .field("dataset_id", documentVectorBean.getDataset_id())
                .field("indexName", documentVectorBean.getIndexName())
                .field("document_title", documentVectorBean.getDocument_title())
                .field("document_content", documentVectorBean.getDocument_content())
                .field("document_embedding", documentVectorBean.getDocument_embedding())
                .field("score", documentVectorBean.getScore())
               .field("page", documentVectorBean.getPage())
                .field("sourceFile", documentVectorBean.getSourceFile())
                .field("sourceUrl", documentVectorBean.getSourceUrl())
                .field("sourceType", documentVectorBean.getSourceType())
                .field("author", documentVectorBean.getAuthor())
                .endObject();

        Request updateRequest = new Request("POST", "/" + indexName + "/_update/" + documentVectorBean.getId());
        updateRequest.setJsonEntity(Strings.toString(document));
        Response response = restClient.performRequest(updateRequest);
        log.debug("Document updated with status code: " + response.getStatusLine().getStatusCode());
    }

    @SneakyThrows
    @Override
    public void deleteVectorIndex(String indexName, long documentId) {
        // 根据DocumentVectorBean的字段id删除文档内容
        
        // TODO 待处理成业务id
        Request deleteRequest = new Request("DELETE", "/" + indexName + "/_doc/" + documentId);
        Response response = restClient.performRequest(deleteRequest);
        log.debug("Document deleted with status code: " + response.getStatusLine().getStatusCode());
    }

}
