package com.alinesno.infra.base.document.service.impl;

import com.alinesno.infra.base.document.entity.SearchLogEntity;
import com.alinesno.infra.base.document.mapper.SearchLogMapper;
import com.alinesno.infra.base.document.service.ISearchLogService;
import org.springframework.stereotype.Service;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;

/**
 * 搜索日志Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
public class SearchLogServiceImpl extends IBaseServiceImpl<SearchLogEntity, SearchLogMapper> implements ISearchLogService {
}