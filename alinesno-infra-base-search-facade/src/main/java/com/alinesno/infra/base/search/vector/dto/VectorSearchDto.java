package com.alinesno.infra.base.search.vector.dto;

import lombok.Data;

@Data
public class VectorSearchDto {

    private long datesetId ; // 数据集ID
    private String collectionName ;
    private String searchText ;
    private Integer topK ;

}
