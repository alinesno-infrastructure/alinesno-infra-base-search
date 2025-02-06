package com.alinesno.infra.base.search.memory.controller;

import com.alinesno.infra.common.facade.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能体记忆库控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/base/search/memoryData")
public class MemoryDataController {

    /**
     * 查询记忆库
     */
    @RequestMapping("/query")
    public R<String> queryMemoryData(String memoryId) {

        return R.ok("数据查询成功");
    }

    /**
     * 增加记忆
     * @param memoryId
     * @param data
     * @return
     */
    @PutMapping("/add")
    public R<String> addMemoryData(String memoryId, String data) {

        return R.ok("数据添加成功");
    }

    /**
     * 删除记忆库
     * @param memoryId
     * @param data
     * @return
     */
    @DeleteMapping("/delete")
    public R<String> deleteMemoryData(String memoryId, String data) {

        return R.ok("数据删除成功");
    }

}
