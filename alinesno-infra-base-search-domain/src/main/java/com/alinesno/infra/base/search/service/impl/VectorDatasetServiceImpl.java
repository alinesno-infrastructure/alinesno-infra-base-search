package com.alinesno.infra.base.search.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.adapter.consumer.EmbeddingConsumer;
import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.mapper.VectorDatasetMapper;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.base.search.vector.dto.InsertField;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用构建Service业务层处理
 * 
 * @version 1.0.0
 * @since 2023-09-30
 */
@Slf4j
@Service
public class VectorDatasetServiceImpl extends IBaseServiceImpl<VectorDatasetEntity, VectorDatasetMapper> implements IVectorDatasetService {

    @Autowired
    private IMilvusDataService milvusDataService;

    private static final Gson gson = new Gson() ;

    @Autowired
    private EmbeddingConsumer embeddingConsumer ;

    @Override
    public void insertDatasetKnowledge(Long datasetId, List<String> sentenceList) {

        String datasetIdStr = datasetId+"" ;

        VectorDatasetEntity vectorDatasetEntity = getById(datasetId) ;
        vectorDatasetEntity.setDatasetSize(sentenceList.size() + vectorDatasetEntity.getDatasetSize());
        update(vectorDatasetEntity) ;

        for(String content : sentenceList){
            List<InsertField> insertFieldData = new ArrayList<>();

            Object vectorData = embeddingConsumer.embeddings(gson.toJson(List.of(new EmbeddingText(content)))) ;

            log.debug("vectorData = {}" , vectorData);

            insertFieldData.add(new InsertField("id", IdUtil.getSnowflakeNextIdStr()));
            insertFieldData.add(new InsertField("datasetId", datasetIdStr));
            insertFieldData.add(new InsertField("documentContent", vectorData));

            milvusDataService.insertData(datasetIdStr , "novel" , insertFieldData);
        }
    }

    @Data
    @AllArgsConstructor
    private static class EmbeddingText{
        String text ;
    }
}