package com.alinesno.infra.base.search.gateway.controller;

import com.alinesno.infra.base.search.api.SearchRequestDto;
import com.alinesno.infra.base.search.entity.DocumentsEntity;
import com.alinesno.infra.base.search.gateway.utils.search.DataGenerator;
import com.alinesno.infra.base.search.gateway.utils.search.DocumentSearchUtils;
import com.alinesno.infra.base.search.gateway.utils.search.TimeSplitBean;
import com.alinesno.infra.base.search.service.IDocumentsService;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.core.utils.DateUtils;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 处理与DocumentEntity相关的请求的Controller。
 * 继承自BaseController类并实现IDocumentService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Api(tags = "Document")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/base/search/documents")
public class DocumentsController extends BaseController<DocumentsEntity, IDocumentsService> {
    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(DocumentsController.class);

    @Autowired
    private IDocumentsService service;

    @Autowired
    private IElasticsearchDocumentService elasticsearchService ;

    /**
     * 获取DocumentEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));

        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);

        SearchRequestDto dto = new SearchRequestDto() ;
        dto.setIndexBase("products");
        dto.setFieldName("email");
        dto.setQueryText("@example.com");
        dto.setCurrentPage(page.getPageNum());
        dto.setPageSize(10);

        List<Map<String , Object>> data = elasticsearchService.searchFieldByPage(dto) ;
        data.forEach(item -> {
            item.put("timestamp", DateUtils.dateTimeNow()) ;
        });

        tableDataInfo.setRows(data); ;
        tableDataInfo.setTotal(100); ;

        return tableDataInfo;
    }

    /**
     * 获取DocumentEntity的分页数据。
     * @return
     */
    @GetMapping("/searchHits")
    public AjaxResult searchHits() {

        DocumentSearchUtils utils = new DocumentSearchUtils() ;

        String startTime = "2024-01-12 10:12";
        String endTime = "2024-01-12 12:12";

        List<TimeSplitBean> timeSegments = utils.timeSplit(startTime , endTime ,  1) ;

        List<String> strList = timeSegments.stream().map(TimeSplitBean::getMiddleTime).toList() ;
        List<DataGenerator.ItemObject> dataObjects = DataGenerator.generateItemObjects(strList) ;

        AjaxResult ajaxResult = ok() ;

        ajaxResult.put("dataItems", dataObjects) ;
        ajaxResult.put("timeSegments", strList) ;

        return ajaxResult ;
    }

    @Override
    public IDocumentsService getFeign() {
        return this.service;
    }
}