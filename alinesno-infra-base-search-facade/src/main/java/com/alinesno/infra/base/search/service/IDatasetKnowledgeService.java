package com.alinesno.infra.base.search.service;


import com.alinesno.infra.base.search.entity.DatasetKnowledgeEntity;
import com.alinesno.infra.common.facade.services.IBaseService;

import java.util.List;

/**
 *  应用管理Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IDatasetKnowledgeService extends IBaseService<DatasetKnowledgeEntity> {

    void extracted(Long datasetId, List<String> sentenceList, String fileName);
}