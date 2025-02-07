package com.alinesno.infra.base.search.memory.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class EntryBean {

    public ReflectionBean reflection;

    public List<InformationBean> information;

}