package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.entity.DocumentIndexEntity;
import com.alinesno.infra.base.search.mapper.DocumentIndexMapper;
import com.alinesno.infra.base.search.service.IDocumentIndexService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 文档向量Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
@Slf4j
public class DocumentIndexServiceImpl extends IBaseServiceImpl<DocumentIndexEntity, DocumentIndexMapper> implements IDocumentIndexService {

}