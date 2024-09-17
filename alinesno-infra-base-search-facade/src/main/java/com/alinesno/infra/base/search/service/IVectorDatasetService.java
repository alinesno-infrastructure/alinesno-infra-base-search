package com.alinesno.infra.base.search.service;


import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.dto.VectorSearchDto;
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
     *
     * @param datasetId
     * @param sentenceList
     * @param fileName
     * @param fileType
     */
    void insertDatasetKnowledge(Long datasetId, List<String> sentenceList, String fileName, String fileType);

    /**
     * 向量搜索
     *
     * @param dto
     * @return
     */
    List<DocumentVectorBean> search(VectorSearchDto dto);

    /**
     * 创建数据集
     * @param collectionName
     * @param description
     * @param shardsNum
     */
    void buildCreateCollectionParam(String collectionName, String description, int shardsNum);

    /**
     * 获取向量引擎
     * @return
     */
    String getVectorEngine();

    /**
     * 重新排序搜索
     * @param dto
     * @return
     */
    List<DocumentVectorBean> rerankSearch(VectorSearchDto dto);
}