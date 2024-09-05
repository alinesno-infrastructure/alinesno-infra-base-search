package com.alinesno.infra.base.search.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.entity.ProjectEntity;
import com.alinesno.infra.base.search.mapper.ProjectMapper;
import com.alinesno.infra.base.search.service.IProjectService;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 应用构建Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Slf4j
@Service
public class ProjectServiceImpl extends IBaseServiceImpl<ProjectEntity, ProjectMapper> implements IProjectService {

    private static final String DEFAULT_PROJECT_FIELD = "default" ;

    @Override
    public void initDefaultApp(long userId) {

        String code = IdUtil.nanoId(8);

        ProjectEntity project = new ProjectEntity() ;

        project.setOperatorId(userId);
        project.setFieldProp(DEFAULT_PROJECT_FIELD);

        project.setProjectName("默认应用");
        project.setProjectDesc("包含所有的应用查询看权限，用于开发和验证场景");
        project.setProjectCode(code);

        save(project) ;
    }

}