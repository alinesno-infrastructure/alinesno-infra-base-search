package com.alinesno.infra.base.search.memory.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

/**
 * 用于存储解析后的信息实体。
 */
@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformationBean {

    @JSONField(name = "sentence_id")
    public int sentenceId;

    @JSONField(name = "time_info")
    public String timeInfo;

    @JSONField(name = "key_information")
    public String keyInformation;

    @JSONField(name = "keywords")
    public String keywords;
}