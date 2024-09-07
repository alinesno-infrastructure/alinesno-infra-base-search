package com.alinesno.infra.base.search.api;

import com.alinesno.infra.common.facade.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class IndexInfoDto extends BaseDto {

    private String indexName;
    private long storageSize; // 存储大小，单位为字节
    private int docCount; // 文档数量
    private int shardNum; // 分片数
    private String healthStatus; // 健康状态

    // 构造函数
    public IndexInfoDto(String indexName, long storageSize, int docCount, int shardNum, String healthStatus) {
        this.indexName = indexName;
        this.storageSize = storageSize;
        this.docCount = docCount;
        this.shardNum = shardNum;
        this.healthStatus = healthStatus;
    }

}