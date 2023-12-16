package com.alinesno.infra.base.search.vector.dto;

import lombok.Data;

@Data
public class VectorSearchDto {

    private String collectionName ;
    private String searchText ;
    private Integer topK ;

}
