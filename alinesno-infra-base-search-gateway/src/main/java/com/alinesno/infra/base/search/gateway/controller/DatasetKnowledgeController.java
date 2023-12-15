package com.alinesno.infra.base.search.gateway.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.constants.FileTypeEnums;
import com.alinesno.infra.base.search.entity.DatasetKnowledgeEntity;
import com.alinesno.infra.base.search.service.IDatasetKnowledgeService;
import com.alinesno.infra.base.search.service.IDocumentParserService;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.enums.HasStatusEnums;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 处理与BusinessLogEntity相关的请求的Controller。
 * 继承自BaseController类并实现IBusinessLogService接口。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/base/search/datasetKnowledge")
public class DatasetKnowledgeController extends BaseController<DatasetKnowledgeEntity, IDatasetKnowledgeService> {

    @Autowired
    private IDatasetKnowledgeService service;

    @Autowired
    private IVectorDatasetService vectorDatasetService ;

    @Autowired
    private IMilvusDataService milvusDataService;

    @Autowired
    private IDocumentParserService documentParserService ;

    @Value("${alinesno.file.local.path}")
    private String localPath  ;

    /**
     * 获取BusinessLogEntity的DataTables数据。
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
        return this.toPage(model, this.getFeign(), page);
    }

    /**
     * 导入数据
     *
     * @param file 导入文件
     */
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult importData(@RequestPart("file") MultipartFile file, Long datasetId) throws Exception {

        // 获取原始文件名
        String fileName = file.getOriginalFilename();
        List<String> sentenceList = new ArrayList<>();

        // 新生成的文件名称
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String newFileName = IdUtil.getSnowflakeNextId() + "." + fileSuffix;

        // 复制文件
        File targetFile = new File(localPath , newFileName);
        FileUtils.writeByteArrayToFile(targetFile, file.getBytes());

        FileTypeEnums constants = FileTypeEnums.getByValue(fileSuffix.toLowerCase()) ;
        assert constants != null;

        sentenceList = switch (constants) {
            case PDF ->  documentParserService.parsePDF(targetFile);
            case MD -> documentParserService.parseMD(targetFile);
            case EXCEL -> documentParserService.parseExcel(targetFile);
            case DOCX -> documentParserService.getContentDocx(targetFile);
            case DOC -> documentParserService.getContentDoc(targetFile);
            default -> sentenceList;
        };

        log.debug("sentenceList = {}" , new Gson().toJson(sentenceList));

        // 处理完成之后删除文件
        FileUtils.forceDeleteOnExit(targetFile);

        sentenceList = documentParserService.documentParser( sentenceList.get(0) , 500) ;

        // 保存到知识库中
        vectorDatasetService.insertDatasetKnowledge(datasetId , sentenceList) ;

        return AjaxResult.success(fileName) ;
    }

    @Override
    public IDatasetKnowledgeService getFeign() {
        return this.service;
    }
}

