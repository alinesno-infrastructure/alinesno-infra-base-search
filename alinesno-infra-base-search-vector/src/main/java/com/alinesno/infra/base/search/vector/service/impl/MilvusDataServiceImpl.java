package com.alinesno.infra.base.search.vector.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.vector.dto.CollectFieldType;
import com.alinesno.infra.base.search.vector.dto.InsertField;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.param.ConnectParam;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MilvusDataServiceImpl implements IMilvusDataService {

    @Autowired
    private MilvusServiceClient milvusServiceClient;

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

        FieldType fieldType1 = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();

        FieldType fieldType2 = FieldType.newBuilder()
                .withName("dataset_id")
                .withDataType(DataType.Int64)
                .build();

        FieldType fieldType3 = FieldType.newBuilder()
                .withName("document_content")
                .withDataType(DataType.FloatVector)
                .withDimension(2)
                .build();

        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDescription(description)
                .withShardsNum(shardsNum)
                .addFieldType(fieldType1)
                .addFieldType(fieldType2)
                .addFieldType(fieldType3)
                .withEnableDynamicField(true)
                .build();

        milvusServiceClient.createCollection(createCollectionReq) ;
    }

    /**
     * 执行Milvus插入操作的方法。
     * @param collectionName 集合名称。
     * @param partitionName 分区名称。
     * @param fields 字段数据
     */
    @Override
    public void insertData(String collectionName, String partitionName, List<InsertField> fields) {

        List<InsertParam.Field> insertField = new ArrayList<>();

        // 将测试数据转换为插入参数
        for (InsertField field : fields) {
            InsertParam.Field paramField = new InsertParam.Field(field.getFieldName(), Collections.singletonList(field.getFieldValue()));
            insertField.add(paramField);
        }

        // 插入数据到数据集中
        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName(collectionName)
                .withPartitionName(partitionName)
                .withFields(insertField)
                .build();

        milvusServiceClient.insert(insertParam);
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

//    @Override
//    public void doEmbedding(String msg) {
//
//        List<String> sentenceList = new ArrayList<>();
//        sentenceList.add(msg);
//
//        this.save(sentenceList);
//    }
//
//    @Override
//    public void save(List<String> sentenceList){
//
//        List<Integer> contentWordCount = new ArrayList<>();
//        List<List<Float>> contentVector;
//
//        for(String str : sentenceList){
//            contentWordCount.add(str.length());
//        }
//        contentVector = embeddingClient.doEmbedding(sentenceList);
//
//        List<InsertParam.Field> fields = new ArrayList<>();
//        fields.add(new InsertParam.Field("content", sentenceList));
//        fields.add(new InsertParam.Field("content_word_count", contentWordCount));
//        fields.add(new InsertParam.Field("content_vector", contentVector));
//
//        InsertParam insertParam = InsertParam.newBuilder()
//                .withCollectionName("pdf_data")
//                .withFields(fields)
//                .build();
//
//        //插入数据
//        milvusServiceClient.insert(insertParam);
//    }

}
