package com.alinesno.infra.base.search.service;

import java.io.InputStream;
import java.util.List;

public interface IDocumentParserService {

    List<String> parsePDF(InputStream inputStream);

    List<String> getContentDocx(InputStream inputStream);

    List<String> xmindToList(InputStream inputStream);

    List<String> getContentDoc(InputStream inputStream);
}
