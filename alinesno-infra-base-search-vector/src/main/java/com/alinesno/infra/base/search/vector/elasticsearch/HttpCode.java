package com.alinesno.infra.base.search.vector.elasticsearch;

/**
 * 定义了与Elasticsearch操作相关的错误代码接口这些错误代码用于标识不同操作中可能出现的错误
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
public interface HttpCode {

    // 插入数据到Elasticsearch时发生错误的代码
    String ES_INSERT_ERROR =  "03";

    // 从Elasticsearch删除数据时发生错误的代码
    String ES_DELETE_ERROR = "04";

    // 在Elasticsearch中搜索数据时发生错误的代码
    String ES_SEARCH_ERROR = "05";

    // 更新Elasticsearch中的数据时发生错误的代码
    String ES_UPDATE_ERROR = "06";

    // 创建Elasticsearch索引时发生错误的代码
    String INDEX_CREATE_ERROR = "01";

    // 删除Elasticsearch索引时发生错误的代码
    String INDEX_DELETE_ERROR = "02";

}
