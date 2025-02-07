package com.alinesno.infra.base.search.memory.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ReflectionBean {

    @JSONField(name = "basis_and_process")
    public String basisAndProcess;

}