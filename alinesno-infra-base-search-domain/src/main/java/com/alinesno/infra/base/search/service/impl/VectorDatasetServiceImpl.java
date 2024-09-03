package com.alinesno.infra.base.search.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.adapter.consumer.EmbeddingConsumer;
import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.mapper.VectorDatasetMapper;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.dto.EmbeddingBean;
import com.alinesno.infra.base.search.vector.dto.EmbeddingText;
import com.alinesno.infra.base.search.vector.dto.VectorSearchDto;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    @Async
    @Override
    public void insertDatasetKnowledge(Long datasetId, List<String> sentenceList, String fileName, String fileType) {

        VectorDatasetEntity vectorDatasetEntity = getById(datasetId) ;

        int datasetSize = sentenceList.size() + vectorDatasetEntity.getDatasetSize() ;
        log.debug("datasetSize = {}" , datasetSize);

        vectorDatasetEntity.setDatasetSize(datasetSize);
        update(vectorDatasetEntity) ;

        String collectionName = vectorDatasetEntity.getCollectionName();

        for(String content : sentenceList){

            String vectorData = embeddingConsumer.embeddings(gson.toJson(List.of(new EmbeddingText(content)))) ;
            log.debug("vectorData = {}" , vectorData);

            EmbeddingBean embeddingBean = new EmbeddingBean() ;

            embeddingBean.setId(IdUtil.getSnowflakeNextId());
            embeddingBean.setDatasetId(datasetId);
            embeddingBean.setDocumentContent(content);
            embeddingBean.setDocumentVector(vectorData);

            milvusDataService.insertData(collectionName , "novel" , embeddingBean);
        }

        milvusDataService.buildIndexByCollection(collectionName) ;

    }

    @Override
    public List<DocumentVectorBean> search(VectorSearchDto dto) {
        return null;
    }

    @Override
    public void buildCreateCollectionParam(String collectionName, String description, int shardsNum) {

    }

}