package com.alinesno.infra.base.search.adapter.consumer;

import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.dtflys.forest.annotation.*;

import java.util.List;

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
    @Post(url = "/embeddings" , contentType = "application/x-www-form-urlencoded")
    String embeddings(@Body("text") String text) ;

}