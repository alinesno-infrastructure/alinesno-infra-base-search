package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.api.DocumentIndexDto;
import com.alinesno.infra.base.search.entity.DocumentIndexEntity;
import com.alinesno.infra.base.search.mapper.DocumentIndexMapper;
import com.alinesno.infra.base.search.service.IDocumentIndexService;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文档向量Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Service
public class DocumentIndexServiceImpl extends IBaseServiceImpl<DocumentIndexEntity, DocumentIndexMapper> implements IDocumentIndexService {

    @Autowired
    private IElasticsearchDocumentService elasticsearchDocumentService;

    @Override
    public void createDocumentIndex(DocumentIndexDto dto) {

        log.info("创建文档索引：{}", dto);
        String indexName = elasticsearchDocumentService.generateIndexName(dto.getIndexBase(), dto.getIndexType()) ;
        elasticsearchDocumentService.createDocumentIndex(indexName, null) ;

        // 保存索引信息
        DocumentIndexEntity documentIndexEntity = new DocumentIndexEntity() ;
        BeanUtils.copyProperties(dto, documentIndexEntity);

        this.save(documentIndexEntity);
    }
}