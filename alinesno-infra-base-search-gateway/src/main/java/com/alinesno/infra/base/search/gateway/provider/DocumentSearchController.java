package com.alinesno.infra.base.search.gateway.provider;


import cn.hutool.json.JSONUtil;
import com.alinesno.infra.base.search.api.SearchRequestDto;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import com.alinesno.infra.common.facade.response.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文档控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/base/search/document")
public class DocumentSearchController {

    @Autowired
    private IElasticsearchDocumentService elasticsearchService ;

    /**
     * 将数据保存到ES
     *
     * @param jsonObject 要保存的JSON对象
     * @param indexBase  索引基础名称
     * @param indexType  索引类型
     * @return 保存结果信息
     */
    @PostMapping("/save")
    public AjaxResult saveTo(@RequestBody String jsonObject, @RequestParam String indexBase, @RequestParam String indexType) {
        indexType = StringUtils.isBlank(indexType) ? "daily":indexType ;

        elasticsearchService.saveJsonObject(indexBase , indexType , jsonObject) ;
        return AjaxResult.success("保存到Elasticsearch成功");
    }

    /**
     * 保存多条数据到文档
     */
    @PostMapping("/saveBatch")
    public AjaxResult saveToBatch(@RequestBody String objects , @RequestParam String indexBase, @RequestParam String indexType) {
        indexType = StringUtils.isBlank(indexType) ? "daily":indexType ;

        List<String> objectList = JSONUtil.toList(objects , String.class) ;

        elasticsearchService.saveBatchJsonObject(indexBase , indexType , objectList) ;
        return AjaxResult.success("保存到Elasticsearch成功");
    }

    /**
     * 根据查询文本进行搜索
     *
     * @return 搜索结果
     */
    @PostMapping("/searchByPage")
    public AjaxResult searchByPage(@RequestBody SearchRequestDto dto) {
        List<Map<String , Object>> data = elasticsearchService.searchByPage(dto) ;
        return AjaxResult.success(data) ;
    }

    /**
     * 根据字段进行搜索
     *
     * @return 搜索结果
     */
    @PostMapping("/searchFieldByPage")
    public AjaxResult searchFieldByPage(@RequestBody SearchRequestDto dto) {
        List<Map<String , Object>> data = elasticsearchService.searchFieldByPage(dto)  ;
        return AjaxResult.success(data) ;
    }

    /**
     * 删除对象信息
     * @param indexBase
     * @param documentId
     * @return
     */
    @DeleteMapping("/delete")
    public AjaxResult deleteObject(@RequestParam String indexBase, @RequestParam Long documentId) {
        elasticsearchService.deleteDocument(indexBase, documentId);
        return AjaxResult.success("删除成功");
    }

    /**
     * 更新对象信息
     * @param jsonObject
     * @param indexBase
     * @param indexType
     * @return
     */
    @PutMapping("/update")
    public AjaxResult updateObject(@RequestBody String jsonObject, @RequestParam String indexBase, @RequestParam String indexType) {
        // TODO 实现更新逻辑

        return AjaxResult.success("更新成功");
    }

    /**
     * 创建索引
     * @param indexName
     * @return
     */
    @GetMapping("/createIndex")
    public AjaxResult createIndex(@RequestParam(required = true) String indexName) {
        String indexType = "daily";
        elasticsearchService.createDocumentIndex(indexName , indexType);
        return AjaxResult.success("创建索引成功");
    }

}
