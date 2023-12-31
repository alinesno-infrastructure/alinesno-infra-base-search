package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.service.IDocumentParserService;
import com.alinesno.infra.base.search.utils.DocumentParser;
import com.alinesno.infra.base.search.utils.parse.ExcelParser;
import com.alinesno.infra.base.search.utils.parse.MarkdownParser;
import com.alinesno.infra.base.search.utils.parse.PDFParser;
import com.alinesno.infra.base.search.utils.parse.WordParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档解析服务实现类，实现了文档解析服务接口中定义的方法
 */
@Slf4j
@Component
public class DocumentParserServiceImpl implements IDocumentParserService {

    @Override
    public List<String> documentParser(String text, int maxLength) {
        return DocumentParser.parseDocument(text , maxLength);
    }

    /**
     * 解析PDF文档并返回文本内容列表
     */
    @Override
    public List<String> parsePDF(File file) {
        String text = PDFParser.parsePDF(file.getAbsolutePath()) ;
        return List.of(text);
    }

    /**
     * 获取DOCX文档的内容列表
     */
    @Override
    public List<String> getContentDocx(File file) {
        return WordParser.searchWordDocX(file.getAbsolutePath());
    }

    /**
     * 将XMind文件转换为文本内容列表
     */
    @Override
    public List<String> xmindToList(File file) {
        return null;
    }

    /**
     * 获取DOC文件的内容列表
     */
    @Override
    public List<String> getContentDoc(File file) {
        return WordParser.searchWordDoc(file.getAbsolutePath());
    }

    /**
     * 解析Markdown文件并返回文本内容列表
     */
    @Override
    public List<String> parseMD(File file) {
        try {
            String text = MarkdownParser.convertToText(FileUtils.readFileToString(file , Charset.defaultCharset()));
            return List.of(text) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析Excel文件并返回文本内容列表
     */
    @Override
    public List<String> parseExcel(File file) {
        String text = ExcelParser.parse(file.getAbsolutePath()) ;
        return List.of(text);
    }
}
