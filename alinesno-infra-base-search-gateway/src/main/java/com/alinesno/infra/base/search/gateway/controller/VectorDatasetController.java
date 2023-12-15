package com.alinesno.infra.base.search.gateway.controller;

import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

/**
 * 应用构建Controller
 * 处理与ApplicationEntity相关的请求
 * 继承自BaseController类并实现IApplicationService接口
 * 
 * @version 1.0.0
 * @since 2023-09-30
 */
@Slf4j
@Api(tags = "VectorDataset")
@RestController
@Scope("prototype")
@RequestMapping("/api/infra/base/search/vectorDataset")
public class VectorDatasetController extends BaseController<VectorDatasetEntity, IVectorDatasetService> {

    @Autowired
    private IVectorDatasetService service;

    @Autowired
    private IMilvusDataService milvusDataService;

    /**
     * 获取ApplicationEntity的DataTables数据
     * 
     * @param request HttpServletRequest对象
     * @param model Model对象
     * @param page DatatablesPageBean对象
     * @return 包含DataTables数据的TableDataInfo对象
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public AjaxResult save(Model model, @RequestBody VectorDatasetEntity entity) throws Exception {

        String collectionName = generateUniqueString() ;
        String description = entity.getDescription() ;
        int shardsNum = 1 ;

        entity.setCollectionName(collectionName);
        milvusDataService.buildCreateCollectionParam(collectionName, description, shardsNum);

        log.debug("buildCreateCollectionParam = " + collectionName);

        return super.save(model, entity);
    }

    public static String generateUniqueString() {
        int length = 8;
        String characters = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    @Override
    public IVectorDatasetService getFeign() {
        return this.service;
    }
}