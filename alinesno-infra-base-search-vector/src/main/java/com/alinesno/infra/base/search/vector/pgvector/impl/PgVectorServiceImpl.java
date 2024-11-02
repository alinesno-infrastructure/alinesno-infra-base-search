package com.alinesno.infra.base.search.vector.pgvector.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.base.search.vector.DocumentVectorBean;
import com.alinesno.infra.base.search.vector.service.IPgVectorService;
import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import com.pgvector.PGvector;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Component
public class PgVectorServiceImpl implements IPgVectorService {

    @Resource(name = "pgvectorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DashScopeEmbeddingUtils dashScopeEmbeddingUtils;

    @SneakyThrows
    @Override
    public List<DocumentVectorBean> queryDocument(String indexName, String fileName, String queryText, int size) {
        // 获取查询向量
        List<Double> embeddingVector = dashScopeEmbeddingUtils.getEmbeddingDoubles(queryText);
        Object[] queryParams = new Object[] { indexName, fileName, new PGvector(embeddingVector), size };

        // 查询语句
        String querySql = "SELECT *, 1 - (document_embedding <=> ?) AS cosine_similarity " +
                "FROM " + indexName +
                " WHERE source_file = ? AND (document_title ILIKE ? OR document_content ILIKE ?) " +
                "ORDER BY cosine_similarity DESC LIMIT ?";

        // 构建查询条件中的 LIKE 语句
        String likeCondition = "%" + queryText + "%";
        queryParams = new Object[] { indexName, fileName, likeCondition, likeCondition, new PGvector(embeddingVector), size };

        // 执行查询
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(querySql, queryParams);

        // 将查询结果转换为 DocumentVectorBean 对象列表
        List<DocumentVectorBean> results = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            DocumentVectorBean documentVectorBean = new DocumentVectorBean();
            documentVectorBean.setId((Long) row.get("id"));
            documentVectorBean.setDataset_id((Long) row.get("dataset_id"));
            documentVectorBean.setIndexName((String) row.get("index_name"));
            documentVectorBean.setDocument_title((String) row.get("document_title"));
            documentVectorBean.setDocument_desc((String) row.get("document_desc"));
            documentVectorBean.setDocument_content((String) row.get("document_content"));
            documentVectorBean.setDocument_embedding((String) row.get("document_embedding"));
            documentVectorBean.setScore(((Number) row.get("cosine_similarity")).floatValue());
            documentVectorBean.setPage(((Number) row.get("page")).intValue());
            documentVectorBean.setDoc_chunk((String) row.get("doc_chunk"));
            documentVectorBean.setToken_size(((Number) row.get("token_size")).intValue());
            documentVectorBean.setSourceFile((String) row.get("source_file"));
            documentVectorBean.setSourceUrl((String) row.get("source_url"));
            documentVectorBean.setSourceType((String) row.get("source_type"));
            documentVectorBean.setAuthor((String) row.get("author"));

            results.add(documentVectorBean);
        }

        return results;
    }

    @SneakyThrows
    @Override
    public List<DocumentVectorBean> queryVectorDocument(String indexName, String queryText, int size) {

        List<Double> embeddingVector = dashScopeEmbeddingUtils.getEmbeddingDoubles(queryText);
        Object[] neighborParams = new Object[] { new PGvector(embeddingVector) , size };

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * , 1 - (document_embedding <=> ?) AS cosine_similarity FROM "+indexName+" ORDER BY cosine_similarity DESC LIMIT ?", neighborParams );

        // 将查询结果转换为 DocumentVectorBean 对象列表
        List<DocumentVectorBean> results = new ArrayList<>();

        for (Map<String, Object> row : rows) {

            DocumentVectorBean documentVectorBean = new DocumentVectorBean();
            documentVectorBean.setId((Long) row.get("id"));
            documentVectorBean.setDataset_id((Long) row.get("dataset_id"));
            documentVectorBean.setIndexName((String) row.get("index_name"));
            documentVectorBean.setDocument_title((String) row.get("document_title"));
            documentVectorBean.setDocument_desc((String) row.get("document_desc"));
            documentVectorBean.setDocument_content((String) row.get("document_content"));
            documentVectorBean.setScore(((Number) row.get("cosine_similarity")).floatValue());
            documentVectorBean.setPage(((Number) row.get("page")).intValue());
            documentVectorBean.setDoc_chunk((String) row.get("doc_chunk"));
            documentVectorBean.setToken_size(((Number) row.get("token_size")).intValue());
            documentVectorBean.setSourceFile((String) row.get("source_file"));
            documentVectorBean.setSourceUrl((String) row.get("source_url"));
            documentVectorBean.setSourceType((String) row.get("source_type"));
            documentVectorBean.setAuthor((String) row.get("author"));

            results.add(documentVectorBean);
        }

          return results;
    }

    @Override
    public void createVectorIndex(String indexName) {
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");

        String ddl = "CREATE TABLE IF NOT EXISTS "+indexName+" (\n" +
                "    id BIGSERIAL PRIMARY KEY,\n" +
                "    dataset_id BIGINT NOT NULL,\n" +
                "    index_name VARCHAR(255) NOT NULL,\n" +
                "    document_title TEXT,\n" +
                "    document_desc text,  \n" +
                "    document_content TEXT,\n" +
                "    document_embedding VECTOR (1536),\n" +
                "    token_size int,  \n" +
                "    doc_chunk text,  \n" +
                "    score REAL,\n" +
                "    page INTEGER,\n" +
                "    source_file VARCHAR(255),\n" +
                "    source_url TEXT,\n" +
                "    source_type VARCHAR(50),\n" +
                "    author VARCHAR(255)\n" +
                ");"  ;

        jdbcTemplate.execute(ddl) ;
        jdbcTemplate.execute("CREATE INDEX ON "+indexName+" USING ivfflat (document_embedding vector_cosine_ops) WITH (lists = 100);");
    }

    @SneakyThrows
    @Override
    public void insertVector(DocumentVectorBean documentVectorBean) {

        String indexName = documentVectorBean.getIndexName() ;

        // 将文本向量字符串转换为 PGvector 对象
        List<Double> embeddingVector = dashScopeEmbeddingUtils.getEmbeddingDoubles(documentVectorBean.getDocument_content());
        Object[] insertParams = getObjects(documentVectorBean, embeddingVector);

        // 执行插入操作
        int count = jdbcTemplate.update(
                "INSERT INTO "+indexName+" " +
                        "(id, dataset_id, index_name, document_title, document_desc, document_content, document_embedding, token_size, doc_chunk, score, page, source_file, source_url, source_type, author) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                insertParams
        );
        log.debug("插入了 " + count + " 条记录");
    }

    @NotNull
    private static Object[] getObjects(DocumentVectorBean documentVectorBean, List<Double> embeddingVector) {

        PGvector pgVector = new PGvector(embeddingVector) ;

        // 准备插入参数
        return new Object[] {
                IdUtil.getSnowflakeNextId(),
                documentVectorBean.getDataset_id(),
                documentVectorBean.getIndexName(),
                documentVectorBean.getDocument_title(),
                documentVectorBean.getDocument_desc(),
                documentVectorBean.getDocument_content(),
                pgVector,
                documentVectorBean.getToken_size(),
                documentVectorBean.getDoc_chunk(),
                documentVectorBean.getScore(),
                documentVectorBean.getPage(),
                documentVectorBean.getSourceFile(),
                documentVectorBean.getSourceUrl(),
                documentVectorBean.getSourceType(),
                documentVectorBean.getAuthor()
        };
    }

    @SneakyThrows
    @Override
    public void updateVector(DocumentVectorBean documentVectorBean) {
        String indexName = documentVectorBean.getIndexName();

        List<Double> embeddingVector = dashScopeEmbeddingUtils.getEmbeddingDoubles(documentVectorBean.getDocument_content());
        Object[] updateParams = getObjects(documentVectorBean, embeddingVector);

        int count = jdbcTemplate.update(
                "UPDATE " + indexName + " SET " +
                        "dataset_id = ?, " +
                        "index_name = ?, " +
                        "document_title = ?, " +
                        "document_desc = ?, " +
                        "document_content = ?, " +
                        "document_embedding = ?, " +
                        "token_size = ?, " +
                        "doc_chunk = ?, " +
                        "score = ?, " +
                        "page = ?, " +
                        "source_file = ?, " +
                        "source_url = ?, " +
                        "source_type = ?, " +
                        "author = ? " +
                        "WHERE id = ?",
                updateParams[1], // dataset_id
                updateParams[2], // index_name
                updateParams[3], // document_title
                updateParams[4], // document_desc
                updateParams[5], // document_content
                updateParams[6], // document_embedding
                updateParams[7], // token_size
                updateParams[8], // doc_chunk
                updateParams[9], // score
                updateParams[10], // page
                updateParams[11], // source_file
                updateParams[12], // source_url
                updateParams[13], // source_type
                updateParams[14], // author
                documentVectorBean.getId() // id
        );

        log.debug("更新了 " + count + " 条记录");
    }

    @Override
    public void deleteVectorIndex(String indexName, long documentId) {
        int count = jdbcTemplate.update(
                "DELETE FROM " + indexName + " WHERE id = ?",
                documentId
        );

        log.debug("删除了 " + count + " 条记录");
    }

}
