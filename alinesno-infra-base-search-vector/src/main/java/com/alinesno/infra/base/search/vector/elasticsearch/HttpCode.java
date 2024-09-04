package com.alinesno.infra.base.search.vector.elasticsearch;

public interface HttpCode {
    String INDEX_CREATE_ERROR = "01" ;
    String INDEX_DELETE_ERROR = "02" ;
    String ES_INSERT_ERROR =  "03";
    String ES_DELETE_ERROR = "04";
    String ES_SEARCH_ERROR = "05";
}
