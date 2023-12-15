package com.alinesno.infra.base.search.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 文档解析服务接口，定义了对不同类型文档的解析方法
 */
public interface IDocumentParserService {

    /**
     * 解析PDF文档并返回文本内容列表
     */
    List<String> parsePDF(File file);

    /**
     * 获取DOCX文档的内容列表
     */
    List<String> getContentDocx(File file);

    /**
     * 将XMind文件转换为文本内容列表
     */
    List<String> xmindToList(File file);

    /**
     * 获取DOC文件的内容列表
     */
    List<String> getContentDoc(File file);

    /**
     * 解析Markdown文件并返回文本内容列表
     */
    List<String> parseMD(File file);

    /**
     * 解析Excel文件并返回文本内容列表
     */
    List<String> parseExcel(File file);
}
