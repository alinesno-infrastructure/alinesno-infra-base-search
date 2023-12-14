package com.alinesno.infra.base.search.service.impl;

import com.alinesno.infra.base.search.service.IDocumentParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class DocumentParserServiceImpl implements IDocumentParserService {

    @Override
    public List<String> parsePDF(InputStream inputStream) {
        return null;
    }

    @Override
    public List<String> getContentDocx(InputStream inputStream) {
        return null;
    }

    @Override
    public List<String> xmindToList(InputStream inputStream) {
        return null;
    }

    @Override
    public List<String> getContentDoc(InputStream inputStream) {
        return null;
    }
}
