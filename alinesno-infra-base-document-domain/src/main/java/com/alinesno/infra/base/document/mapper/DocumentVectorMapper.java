package com.alinesno.infra.base.document.mapper;

import com.alinesno.infra.base.document.entity.DocumentVectorEntity;
import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文档向量Mapper接口
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Mapper
public interface DocumentVectorMapper extends IBaseMapper<DocumentVectorEntity> {
}