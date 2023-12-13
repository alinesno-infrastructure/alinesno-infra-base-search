package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.entity.UserAuthEntity;
import com.alinesno.infra.base.search.mapper.UserAuthMapper;
import com.alinesno.infra.base.search.service.IUserAuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;

/**
 * 用户身份验证和权限控制Service业务层处理
 *
 * @author  luoxiaodong
 * @version  1.0.0
 */
@Service
@Slf4j
public class UserAuthServiceImpl extends IBaseServiceImpl<UserAuthEntity, UserAuthMapper> implements IUserAuthService {

}