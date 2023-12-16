package com.alinesno.infra.base.search.vector.service.impl;

import com.alinesno.infra.base.search.adapter.consumer.EmbeddingConsumer;
import com.alinesno.infra.base.search.vector.dto.EmbeddingBean;
import com.alinesno.infra.base.search.vector.dto.EmbeddingText;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.MutationResult;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MilvusDataServiceImpl implements IMilvusDataService {

    @Autowired
    private MilvusServiceClient milvusServiceClient;

    @Autowired
    private EmbeddingConsumer embeddingConsumer ;

    /**
     * 创建包含指定字段类型的集合的参数构建方法。
     * @param collectionName 集合名称。
     * @param description 集合描述。
     * @param shardsNum 分片数量。
     */
    @Override
    public void buildCreateCollectionParam(String collectionName,
                                           String description,
                                           int shardsNum) {

        FieldType primaryIdType = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();

        FieldType contentType = FieldType.newBuilder()
                .withName("dataset_id")
                .withDataType(DataType.Int64)
                .build();

        FieldType embeddingsType = FieldType.newBuilder()
                .withName("document_content")
                .withDataType(DataType.FloatVector)
                .withDimension(768)
                .build();

        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDescription(description)
                .withShardsNum(shardsNum)

                .addFieldType(primaryIdType)
                .addFieldType(contentType)
                .addFieldType(embeddingsType)

                .withEnableDynamicField(true)
                .build();

        R<RpcStatus> response =  milvusServiceClient.createCollection(createCollectionReq) ;
        log.debug("response = {}" , response.toString());

    }

    /**
     * 执行Milvus插入操作的方法。
     * @param collectionName 集合名称。
     * @param partitionName 分区名称。
     * @param embeddingBean 字段数据
     */
    @Override
    public void insertData(String collectionName, String partitionName, EmbeddingBean embeddingBean) {

        List<InsertParam.Field> insertField = new ArrayList<>();

        // 将测试数据转换为插入参数
        List<List<Float>> embeddings = populateEmbeddings(embeddingBean.getDocumentContent());

        log.debug("embeddings = {}" , embeddings);

        insertField.add(new InsertParam.Field("id" , Collections.singletonList(embeddingBean.getId()))) ;
        insertField.add(new InsertParam.Field("dataset_id" , Collections.singletonList(embeddingBean.getDatasetId()))) ;
        insertField.add(new InsertParam.Field("document_content" , embeddings)) ;

        // 插入数据到数据集中
        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName(collectionName)
                .withPartitionName(partitionName)
                .withFields(insertField)
                .build();

        R<MutationResult> response = milvusServiceClient.insert(insertParam);
        log.debug("response = {}" , response);

    }

    /**
     * 处理数据转换成向量的问题
     * @param vectorData
     * @return
     */
    private List<List<Float>> populateEmbeddings(String vectorData) {

        List<List<Float>> embeddingsList = new ArrayList<>() ;

        Gson gson = new GsonBuilder().registerTypeAdapter(Float.class, (com.google.gson.JsonDeserializer<Float>) (json, typeOfT, context) -> json.getAsJsonPrimitive().getAsFloat()).create();
        Float[] doubleArray = gson.fromJson(vectorData, Float[].class);

        log.debug("vectorData = {}" , vectorData);

        // 检查解析后的数组是否为空
        if (doubleArray != null) {
            int chunkSize = 768; // 每个嵌入向量的维度大小，根据实际情况调整
            for (int i = 0; i < doubleArray.length; i += chunkSize) {
                List<Float> sublist = new ArrayList<>();
                for (int j = i; j < i + chunkSize && j < doubleArray.length; j++) {
                    sublist.add(doubleArray[j]);
                }
                embeddingsList.add(sublist);
            }
        }

        // 将String转换成ListFloat
        log.debug("------->>>>>>>>>>>>>>>>>>>>>>-------");
        log.debug("vectorFloatData = {}" , new Gson().toJson(doubleArray));

        return embeddingsList ;
    }

    /**
     * 执行Milvus删除操作的方法。
     * @param collectionName 集合名称。
     * @param deleteExpr 删除表达式。
     */
    @Override
    public void deleteData(String collectionName, String deleteExpr) {
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withCollectionName(collectionName)
                .withExpr(deleteExpr)
                .build();
        milvusServiceClient.delete(deleteParam);
    }


    @Override
    public List<List<Float>> textToVector(String searchText) {

        String vectorData = embeddingConsumer.embeddings(new Gson().toJson(List.of(new EmbeddingText(searchText)))) ;
        log.debug("vectorData = {}" , vectorData);

        return populateEmbeddings(vectorData) ;
    }

}
