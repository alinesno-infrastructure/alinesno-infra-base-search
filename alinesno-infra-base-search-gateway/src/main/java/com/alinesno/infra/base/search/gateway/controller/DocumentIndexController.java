package com.alinesno.infra.base.search.gateway.controller;

import com.alinesno.infra.base.search.api.DocumentIndexDto;
import com.alinesno.infra.base.search.api.IndexInfoDto;
import com.alinesno.infra.base.search.entity.DocumentIndexEntity;
import com.alinesno.infra.base.search.service.IDocumentIndexService;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.extend.datasource.annotation.DataPermissionScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处理与DocumentVectorEntity相关的请求的Controller。
 * 继承自BaseController类并实现IDocumentVectorService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "DocumentVector")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/base/search/documentIndex")
public class DocumentIndexController extends BaseController<DocumentIndexEntity, IDocumentIndexService> {

    @Autowired
    private IDocumentIndexService service;

    @Autowired
    private IElasticsearchDocumentService elasticsearchDocumentService;

    /**
     * 获取DocumentVectorEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @DataPermissionScope
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));

        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);

        List<DocumentIndexEntity> indexInfoList = (List<DocumentIndexEntity>) tableDataInfo.getRows();
        if(indexInfoList != null){
            indexInfoList.forEach(e -> {

                long totalSize = 0L;
                long documentSize = 0L;
                long indexCount = 0L;

                // 查询索引信息
                List<IndexInfoDto> dtoList = null;

                try {
                    dtoList = elasticsearchDocumentService.getIndexInfo(e.getIndexBase());

                    totalSize = dtoList.stream().mapToLong(IndexInfoDto::getStorageSize).sum();
                    documentSize = dtoList.stream().mapToLong(IndexInfoDto::getDocCount).sum();
                    indexCount = dtoList.size();

                } catch (Exception ex) {
                    log.error("查询索引信息失败", ex);
                }

                e.setInfoList(dtoList);
                e.setStorageSize(totalSize);
                e.setDocCount(documentSize);
                e.setIndexCount(indexCount);
            });
        }

        tableDataInfo.setRows(indexInfoList);

        return tableDataInfo;

    }

    /**
     * 创建索引
     * @return
     */
    @PostMapping("/createDocumentIndex")
    public AjaxResult createDocumentIndex(@RequestBody DocumentIndexDto dto){
        service.createDocumentIndex(dto);
        return ok() ;
    }

    @Override
    public IDocumentIndexService getFeign() {
        return this.service;
    }
}