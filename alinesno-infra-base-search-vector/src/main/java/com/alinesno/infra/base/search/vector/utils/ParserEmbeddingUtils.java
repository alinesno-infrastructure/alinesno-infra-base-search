package com.alinesno.infra.base.search.vector.utils;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ParserEmbeddingUtils {
    /**
     * 处理数据转换成向量的问题
     *
     * @param vectorData
     * @return
     */
    public static Float[] populateEmbeddings(String vectorData) {

        List<List<Float>> embeddingsList = new ArrayList<>() ;

        Gson gson = new GsonBuilder().registerTypeAdapter(Float.class, (com.google.gson.JsonDeserializer<Float>) (json, typeOfT, context) -> json.getAsJsonPrimitive().getAsFloat()).create();
        Float[] doubleArray = gson.fromJson(vectorData, Float[].class);

        log.debug("vectorData = {}" , vectorData);
        System.out.println("vectorData = " + JSONUtil.toJsonPrettyStr(vectorData));

        return doubleArray ;
    }

}
