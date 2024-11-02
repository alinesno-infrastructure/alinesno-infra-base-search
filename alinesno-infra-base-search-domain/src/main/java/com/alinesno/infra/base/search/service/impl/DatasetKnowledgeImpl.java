package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.entity.DatasetKnowledgeEntity;
import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.mapper.DatasetKnowledgeMapper;
import com.alinesno.infra.base.search.service.IDatasetKnowledgeService;
import com.alinesno.infra.base.search.service.IDocumentParserService;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
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

    @Value("${alinesno.base.search.document.max-segment-length:512}")
    private int maxSegmentLength ;

    @Autowired
    private IDocumentParserService documentParserService ;

    @Autowired
    private IVectorDatasetService vectorDatasetService ;

    @Override
    public void parserDocument(Long datasetId, List<String> sentenceList, String fileName, String fileType) {

        // 从DataSet里面获取到长度
        sentenceList = documentParserService.documentParser(sentenceList.get(0) , maxSegmentLength) ;

        DatasetKnowledgeEntity e = new DatasetKnowledgeEntity() ;
        e.setDocumentName(fileName);
        e.setDatasetId(datasetId);
        e.setDocumentDesc(fileName);
        e.setDocumentCount(sentenceList.size());

        log.debug("document count = {}" , e.getDocumentCount());

        save(e) ;

        if(!sentenceList.isEmpty()){
            sentenceList.forEach(s -> log.debug("sentence = {}" , s));
            // 保存到知识库中
            vectorDatasetService.insertDatasetKnowledge(datasetId, sentenceList , fileName , fileType) ;
        }
    }

    @Override
    public void saveBatchToDataset(Long datasetId, List<String> sentenceList , String fileName) {

        // 查询datasetId是否存在数据库
        VectorDatasetEntity entity = vectorDatasetService.getById(datasetId) ;
        Assert.isTrue(entity != null , "datasetId is not exists");

        // 处理文本长度问题
        List<String> newSentenceList = new ArrayList<>() ;
        sentenceList.forEach(s -> {
            List<String> list = documentParserService.documentParser(s , maxSegmentLength) ;
            newSentenceList.addAll(list) ;
        }) ;

        String fileType = "txt" ; // 文本格式
        vectorDatasetService.insertDatasetKnowledge(datasetId, newSentenceList , fileName , fileType) ;
    }
}
