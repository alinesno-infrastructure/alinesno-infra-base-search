package com.alinesno.infra.base.search.adapter.consumer;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;

@BaseRequest(
        baseURL = "#{alinesno.infra.gateway.host}" ,
        connectTimeout = 30*1000 ,
        readTimeout = 60*1000
)
public interface EmbeddingConsumer {

    /**
     * 返回接口数据
     * @param text
     * @return
     */
    @Post(url = "/api/ai/embeddings" , contentType = "application/x-www-form-urlencoded")
    String embeddings(@Body("text") String text) ;

}