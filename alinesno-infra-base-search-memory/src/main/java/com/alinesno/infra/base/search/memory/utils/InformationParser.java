package com.alinesno.infra.base.search.memory.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alinesno.infra.base.search.memory.bean.EntryBean;
import com.alinesno.infra.base.search.memory.bean.InformationBean;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 解析输入内容并提取相关信息的工具类。
 */
@Slf4j
public class InformationParser {

    /**
     * 解析输入的内容并提取相关信息。
     *
     * @param content 输入的文本内容
     * @return 包含解析结果的列表
     */
    public static List<EntryBean> parseContent(String content) {
        try {
            // 解析JSON字符串到List<EntryBean>
            List<EntryBean> entries = JSON.parseObject(content, new TypeReference<List<EntryBean>>() {});

            // 处理解析后的数据
            for (EntryBean entry : entries) {
                System.out.println("Basis and Process: " + entry.reflection.basisAndProcess);
                for (InformationBean info : entry.information) {
                    System.out.println("Sentence ID: " + info.sentenceId + ", Key Information: " + info.keyInformation);
                }
            }

            return entries ;

        } catch (Exception e) {
            log.error("解析输入内容并提取相关信息时出错：" + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}