package com.alinesno.infra.base.search.memory.storage;

import com.alinesno.infra.base.search.memory.BaseMemoryStore;
import com.alinesno.infra.base.search.memory.bean.MemoryNode;
import com.alinesno.infra.base.search.vector.utils.DashScopeEmbeddingUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 记忆库处理
 */
@Slf4j
@Service
public class PgVectorMemoryStore implements BaseMemoryStore {

    @Resource(name = "pgvectorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DashScopeEmbeddingUtils dashScopeEmbeddingUtils;

    // 变量转换成大写
    private static final String ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT = "alinesno_search_vector_document";
    public void createVectorIndex(String indexName) {

        // 调整为只为一个向量库
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");
        String ddl = "CREATE table IF NOT EXISTS "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+" (\n" +
                "                    memory_id UUID NOT NULL DEFAULT gen_random_uuid(), -- 记忆的唯一标识符，默认使用UUID生成\n" +
                "                    user_name VARCHAR, -- 用户名，表示记忆所属的用户\n" +
                "                    target_name VARCHAR, -- 目标名称，表示记忆的目标或对象\n" +
                "                    meta_data JSONB NOT NULL DEFAULT '{}', -- 元数据，用于存储记忆的相关信息，如时间、地点等\n" +
                "                    content TEXT, -- 记忆的内容\n" +
                "                    keys VARCHAR, -- 记忆的键(有多个key存在)\n" +
                "                    keys_vector VECTOR (1536), -- 记忆的向量表示，用于向量搜索和相似度计算\n" +
                "                    value VARCHAR, -- 记忆的值\n" +
                "                    score_recall DOUBLE PRECISION, -- 记忆的召回分数\n" +
                "                    score_rank DOUBLE PRECISION, -- 记忆的排序分数\n" +
                "                    score_rerank DOUBLE PRECISION, -- 记忆的重排序分数\n" +
                "                    memory_type VARCHAR, -- 记忆的类型\n" +
                "                    action_status VARCHAR DEFAULT 'none', -- 动作状态，表示记忆的当前动作状态，默认为\"none\"\n" +
                "                    store_status VARCHAR DEFAULT 'valid', -- 存储状态，表示记忆的存储状态，默认为\"valid\"\n" +
                "                    content_vector VECTOR (1536), -- 记忆的向量表示，可能与keyVector有区别，具体视业务逻辑而定\n" +
                "                    timestamp BIGINT NOT NULL DEFAULT EXTRACT(EPOCH FROM NOW()), -- 记忆的时间戳，确保每个记忆节点的时间戳唯一\n" +
                "                    dt DATE, -- 记忆的日期时间字符串，格式为\"yyyy-MM-dd\"\n" +
                "                    obs_reflected INTEGER DEFAULT 0, -- 观察到的反射次数，用于记录记忆被反射的次数\n" +
                "                    obs_updated INTEGER DEFAULT 0, -- 观察到的更新次数，用于记录记忆被更新的次数\n" +
                "                    PRIMARY KEY (memory_id)\n" +
                "                )";

            jdbcTemplate.execute("CREATE INDEX ON "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+"  USING ivfflat (content_vector vector_cosine_ops) WITH (lists = 100)");
            jdbcTemplate.execute("CREATE INDEX ON "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+" USING ivfflat (keys_vector vector_cosine_ops) WITH (lists = 100)");
            jdbcTemplate.execute("COMMENT ON TABLE "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+" IS '该表用于存储记忆节点，包括记忆内容、元数据、向量表示、以及各种评分和状态信息'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".memory_id IS '记忆的唯一标识符，默认使用UUID生成'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".user_name IS '用户名，表示记忆所属的用户'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".target_name IS '目标名称，表示记忆的目标或对象'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".meta_data IS '元数据，用于存储记忆的相关信息，如时间、地点等'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".content IS '记忆的内容'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".keys IS '记忆的键(有多个key存在)'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".keys_vector IS '记忆的向量表示，用于向量搜索和相似度计算'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".value IS '记忆的值'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".score_recall IS '记忆的召回分数'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".score_rank IS '记忆的排序分数'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".score_rerank IS '记忆的重排序分数'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".memory_type IS '记忆的类型'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".action_status IS '动作状态，表示记忆的当前动作状态，默认为\"none\"'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".store_status IS '存储状态，表示记忆的存储状态，默认为\"valid\"'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".content_vector IS '记忆的向量表示，可能与keyVector有区别，具体视业务逻辑而定'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".timestamp IS '记忆的时间戳，确保每个记忆节点的时间戳唯一'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".dt IS '记忆的日期时间字符串，格式为\"yyyy-MM-dd\"'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".obs_reflected IS '观察到的反射次数，用于记录记忆被反射的次数'");
            jdbcTemplate.execute("COMMENT ON COLUMN "+ALINESNO_SEARCH_VECTOR_MEMORY_DOCUMENT+".obs_updated IS '观察到的更新次数，用于记录记忆被更新的次数'");

        jdbcTemplate.execute(ddl) ;
    }

    @Override
    public void batchInsert(List<MemoryNode> nodes) {

    }

}
