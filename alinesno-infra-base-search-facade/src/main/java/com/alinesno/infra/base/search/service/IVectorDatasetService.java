package com.alinesno.infra.base.search.service;


import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.common.facade.services.IBaseService;

import java.util.List;

/**
 * 应用构建Service接口
 * 
 * @version 1.0.0
 * @since 2023-09-30
 */
public interface IVectorDatasetService extends IBaseService<VectorDatasetEntity> {

    /**
     * 保存到知识库中
     * @param datasetId
     * @param sentenceList
     */
    void insertDatasetKnowledge(Long datasetId, List<String> sentenceList);

}