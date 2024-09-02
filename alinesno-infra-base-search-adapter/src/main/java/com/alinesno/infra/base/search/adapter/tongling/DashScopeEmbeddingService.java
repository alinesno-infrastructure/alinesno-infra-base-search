package com.alinesno.infra.base.search.adapter.tongling;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.embeddings.BatchTextEmbedding;
import com.alibaba.dashscope.embeddings.BatchTextEmbeddingParam;
import com.alibaba.dashscope.embeddings.BatchTextEmbeddingResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.System;
import java.util.Collection;

/**
 * 使用阿里服务完成文本转换 EmbeddingService类用于处理文本嵌入相关操作
 * <a href="https://help.aliyun.com/zh/dashscope/developer-reference/text-embedding-quick-start">参考文档</a>
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Component
public class DashScopeEmbeddingService {

    @Value("${alinesno.base.search.dashscope.api-key}")
    private String apiKey ;

    /**
     * 处理单个文本列表的嵌入
     *
     * @return
     * @throws ApiException      当API调用异常时抛出
     * @throws NoApiKeyException 当没有API密钥时抛出
     */
    public TextEmbeddingResult embedSingleList(Collection<? extends String> texts) throws ApiException, NoApiKeyException {
        // 构建文本嵌入参数
        TextEmbeddingParam param = TextEmbeddingParam
                .builder()
                .model(TextEmbedding.Models.TEXT_EMBEDDING_V1) // 设置模型
                .apiKey(apiKey) // 从环境变量中获取API密钥
                .texts(texts) // 设置待处理的文本列表
                .build();
        // 初始化文本嵌入对象并调用嵌入方法
        TextEmbedding textEmbedding = new TextEmbedding();
        TextEmbeddingResult result = textEmbedding.call(param);
        System.out.println(result);

        return result ;
    }

    /**
     * 处理批量文本嵌入
     *
     * @return
     * @throws ApiException      当API调用异常时抛出
     * @throws NoApiKeyException 当没有API密钥时抛出
     */
    public BatchTextEmbeddingResult embedBatch() throws ApiException, NoApiKeyException {
        // 构建批量文本嵌入参数
        BatchTextEmbeddingParam param = BatchTextEmbeddingParam.builder()
                .model(BatchTextEmbedding.Models.TEXT_EMBEDDING_ASYNC_V1) // 设置模型
                .apiKey(apiKey) // 从环境变量中获取API密钥
                .url("https://modelscope.oss-cn-beijing.aliyuncs.com/resource/text_embedding_file.txt") // 设置文本文件的URL
                .build();
        // 初始化批量文本嵌入对象并调用嵌入方法
        BatchTextEmbedding textEmbedding = new BatchTextEmbedding();
        BatchTextEmbeddingResult result = textEmbedding.call(param);
        System.out.println(result);

        return result ;
    }

}
