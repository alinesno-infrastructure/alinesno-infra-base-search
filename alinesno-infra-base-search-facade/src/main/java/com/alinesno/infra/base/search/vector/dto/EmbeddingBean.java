package com.alinesno.infra.base.search.vector.dto;

import lombok.Data;

/**
 * 业务对接
 */
@Data
public class EmbeddingBean {

    private Long id ;
    private Long datasetId ;
    private String documentContent ;

}
