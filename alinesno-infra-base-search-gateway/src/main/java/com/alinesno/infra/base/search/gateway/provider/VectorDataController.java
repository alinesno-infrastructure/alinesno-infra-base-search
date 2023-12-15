package com.alinesno.infra.base.search.gateway.provider;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.constants.FileTypeEnums;
import com.alinesno.infra.base.search.service.IDocumentParserService;
import com.alinesno.infra.base.search.vector.dto.CollectFieldType;
import com.alinesno.infra.base.search.vector.dto.InsertField;
import com.alinesno.infra.base.search.vector.service.IMilvusDataService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Milvus数据服务控制器，用于定义对Milvus数据库进行操作的REST API接口。
 */
@Slf4j
@RestController
@RequestMapping("/api/base/search/vectorData")
public class VectorDataController {

    @Autowired
    private IDocumentParserService documentParserService ;

    @Autowired
    private IMilvusDataService milvusDataService;

    @Value("${alinesno.file.local.path}")
    private String localPath  ;

    /**
     * 文件上传，支持PDF、Word、Xmind
     * @param file
     * @throws Exception
     */
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws Exception {

        List<String> sentenceList = new ArrayList<>();

        // 新生成的文件名称
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String fileName = IdUtil.getSnowflakeNextId() + "." + fileSuffix;

        // 复制文件
        File targetFile = new File(localPath , fileName);
        FileUtils.writeByteArrayToFile(targetFile, file.getBytes());

        FileTypeEnums constants = FileTypeEnums.getByValue(fileSuffix.toLowerCase()) ;
        assert constants != null;

        sentenceList = switch (constants) {
            case PDF -> documentParserService.parsePDF(targetFile);
            case MD -> documentParserService.parseMD(targetFile);
            case EXCEL -> documentParserService.parseExcel(targetFile);
            case DOCX -> documentParserService.getContentDocx(targetFile);
            case DOC -> documentParserService.getContentDoc(targetFile);
            default -> sentenceList;
        };

        log.debug("sentenceList = {}" , new Gson().toJson(sentenceList));

        // 处理完成之后删除文件
        FileUtils.forceDeleteOnExit(targetFile);

//        milvusDataService.save(sentenceList);
    }


    /**
     * 创建集合的REST API接口
     * @param collectionName 集合名称
     * @param description 集合描述
     * @param shardsNum 分片数量
     */
    @PostMapping("/createCollection")
    public void createCollection(@RequestParam String collectionName,
                                 @RequestParam String description,
                                 @RequestParam int shardsNum) {
        milvusDataService.buildCreateCollectionParam(collectionName, description, shardsNum);
    }

    /**
     * 插入数据的REST API接口
     * @param collectionName 集合名称
     * @param partitionName 分区名称
     * @param fields 插入参数字段列表
     */
    @PostMapping("/insertData")
    public void insertData(@RequestParam String collectionName,
                           @RequestParam String partitionName,
                           @RequestBody List<InsertField> fields) {
        milvusDataService.insertData(collectionName, partitionName, fields);
    }

    /**
     * 删除数据的REST API接口
     * @param collectionName 集合名称
     * @param deleteExpr 删除表达式
     */
    @DeleteMapping("/deleteData")
    public void deleteData(@RequestParam String collectionName,
                           @RequestParam String deleteExpr) {
        milvusDataService.deleteData(collectionName, deleteExpr);
    }
}
