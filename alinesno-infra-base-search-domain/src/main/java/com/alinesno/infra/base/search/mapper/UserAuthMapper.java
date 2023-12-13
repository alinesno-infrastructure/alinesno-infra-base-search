package com.alinesno.infra.base.search.mapper;

import com.alinesno.infra.base.search.entity.UserAuthEntity;
import org.apache.ibatis.annotations.Mapper;
import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;

/**
 * 用户身份验证和权限控制Mapper接口
 *
 * @author  luoxiaodong
 * @version  1.0.0
 */
@Mapper
public interface UserAuthMapper extends IBaseMapper<UserAuthEntity> {
}