package com.alinesno.infra.base.search.vector.elasticsearch.impl;

import com.alinesno.infra.base.search.vector.elasticsearch.EsConfiguration;
import com.alinesno.infra.base.search.vector.service.IElasticsearchDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ElasticsearchDocumentServiceImpl implements IElasticsearchDocumentService {

    @Autowired
    private RestClient restClient ;

    @Override
    public void saveJsonObject(String indexBase, String indexType) {

    }

    @Override
    public List<Map<String, Object>> search(String indexBase, String indexType, String queryText) {
        return null;
    }

    @Override
    public List<Map<String, Object>> search(String indexBase, String indexType, String fieldName, String queryText) {
        return null;
    }
}
