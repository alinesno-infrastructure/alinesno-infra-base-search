package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.entity.DocumentsEntity;
import com.alinesno.infra.base.search.mapper.DocumentsMapper;
import com.alinesno.infra.base.search.service.IDocumentsService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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