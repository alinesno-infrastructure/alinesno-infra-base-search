package com.alinesno.infra.base.search.service;

import com.alinesno.infra.base.search.entity.ProjectEntity;
import com.alinesno.infra.common.facade.services.IBaseService;

/**
 * 应用构建Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IProjectService extends IBaseService<ProjectEntity> {

    /**
     * 初始化默认应用
     * @param orgId
     */
    void initDefaultApp(long orgId);

}