package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.entity.DatasetKnowledgeEntity;
import com.alinesno.infra.base.search.mapper.DatasetKnowledgeMapper;
import com.alinesno.infra.base.search.service.IDatasetKnowledgeService;
import com.alinesno.infra.base.search.service.IDocumentParserService;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用管理Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Service
public class DatasetKnowledgeImpl extends IBaseServiceImpl<DatasetKnowledgeEntity, DatasetKnowledgeMapper> implements IDatasetKnowledgeService {

    @Autowired
    private IDocumentParserService documentParserService ;

    @Autowired
    private IVectorDatasetService vectorDatasetService ;

    @Override
    public void extracted(Long datasetId, List<String> sentenceList, String fileName) {
        sentenceList = documentParserService.documentParser( sentenceList.get(0) , 500) ;

        DatasetKnowledgeEntity e = new DatasetKnowledgeEntity() ;
        e.setDocumentName(fileName);
        e.setDatasetId(datasetId);
        e.setDocumentDesc(fileName);
        e.setDocumentCount(sentenceList.size());

        save(e) ;

        // 保存到知识库中
        vectorDatasetService.insertDatasetKnowledge(datasetId, sentenceList) ;
    }
}
