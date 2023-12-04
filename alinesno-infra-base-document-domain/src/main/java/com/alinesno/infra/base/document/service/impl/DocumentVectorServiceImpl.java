package com.alinesno.infra.base.document.service.impl;

import com.alinesno.infra.base.document.entity.DocumentVectorEntity;
import com.alinesno.infra.base.document.mapper.DocumentVectorMapper;
import com.alinesno.infra.base.document.service.IDocumentVectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;

/**
 * 文档向量Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
@Slf4j
public class DocumentVectorServiceImpl extends IBaseServiceImpl<DocumentVectorEntity, DocumentVectorMapper> implements IDocumentVectorService {

}