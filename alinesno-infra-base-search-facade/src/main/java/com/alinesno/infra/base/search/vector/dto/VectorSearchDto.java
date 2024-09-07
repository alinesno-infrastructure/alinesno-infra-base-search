package com.alinesno.infra.base.search.vector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VectorSearchDto {

    @NotNull(message = "数据集ID不能为空")
    private Long datesetId ; // 数据集ID

    private String collectionName ;

    @NotBlank(message = "搜索文本不能为空")
    private String searchText ;
    private Integer topK ;

}
