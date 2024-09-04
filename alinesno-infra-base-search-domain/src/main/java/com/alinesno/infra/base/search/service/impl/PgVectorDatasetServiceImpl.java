package com.alinesno.infra.base.search.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.mapper.VectorDatasetMapper;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.dto.VectorSearchDto;
import com.alinesno.infra.base.search.vector.service.IPgVectorService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
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
@Primary
@Service("pgVectorDatasetService")
public class PgVectorDatasetServiceImpl extends IBaseServiceImpl<VectorDatasetEntity, VectorDatasetMapper> implements IVectorDatasetService {

    @Autowired
    private IPgVectorService pgVectorService;

    @Async
    @Override
    public void insertDatasetKnowledge(Long datasetId, List<String> sentenceList, String fileName, String fileType) {

        VectorDatasetEntity vectorDatasetEntity = getById(datasetId) ;

        int datasetSize = sentenceList.size() + vectorDatasetEntity.getDatasetSize() ;
        log.debug("datasetSize = {}" , datasetSize);

        vectorDatasetEntity.setDatasetSize(datasetSize);
        update(vectorDatasetEntity) ;

        String collectionName = vectorDatasetEntity.getCollectionName();

        int count = 1 ;
        for(String content : sentenceList){

            DocumentVectorBean embeddingBean = new DocumentVectorBean() ;

            embeddingBean.setId(IdUtil.getSnowflakeNextId());
            embeddingBean.setIndexName(collectionName);
            embeddingBean.setDataset_id(datasetId);
            embeddingBean.setDocument_title(fileName);
            embeddingBean.setDocument_content(content);
            embeddingBean.setPage(count) ;
            embeddingBean.setSourceType(fileType);

            count ++ ;

            pgVectorService.insertVector(embeddingBean);
        }

    }

    @Override
    public List<DocumentVectorBean> search(VectorSearchDto dto) {
        return pgVectorService.queryVectorDocument(dto.getCollectionName() , dto.getSearchText() , dto.getTopK()) ;
    }

    @Override
    public void buildCreateCollectionParam(String collectionName, String description, int shardsNum) {
        pgVectorService.createVectorIndex(collectionName);
    }

}