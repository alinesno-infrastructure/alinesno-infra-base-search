package com.alinesno.infra.base.search.service;

import com.alinesno.infra.base.search.api.DocumentIndexDto;
import com.alinesno.infra.base.search.entity.DocumentIndexEntity;
import com.alinesno.infra.common.facade.services.IBaseService;

/**
 * 文档索引Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */

public interface IDocumentIndexService extends IBaseService<DocumentIndexEntity> {

    /**
     * 创建文档索引
     * @param documentIndexEntity
     */
    void createDocumentIndex(DocumentIndexDto documentIndexEntity);

}
