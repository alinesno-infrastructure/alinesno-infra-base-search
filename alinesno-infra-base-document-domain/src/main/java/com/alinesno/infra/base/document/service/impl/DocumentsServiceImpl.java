package com.alinesno.infra.base.document.service.impl;

import com.alinesno.infra.base.document.entity.DocumentsEntity;
import com.alinesno.infra.base.document.mapper.DocumentsMapper;
import com.alinesno.infra.base.document.service.IDocumentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;

/**
 * 文档Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
@Slf4j
public class DocumentsServiceImpl extends IBaseServiceImpl<DocumentsEntity, DocumentsMapper> implements IDocumentsService {

}