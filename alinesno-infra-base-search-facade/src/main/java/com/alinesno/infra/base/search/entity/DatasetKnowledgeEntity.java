package com.alinesno.infra.base.search.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 数据集知识库
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "dataset_knowledge")
public class DatasetKnowledgeEntity extends InfraBaseEntity {

    @ColumnType(length = 64, value = MySqlTypeConstant.BIGINT)
    @ColumnComment("数据集Id")
    @TableField
    private Long datasetId ;

    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 64)
    @ColumnComment("文档名称")
    @TableField
    private String documentName ; // 文档名称

    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 256)
    @ColumnComment("文档描述")
    @TableField
    private String documentDesc ; // 文档描述

    @ColumnType(MySqlTypeConstant.BIGINT)
    @ColumnComment("文档数量")
    @TableField
    private int documentCount ; // 文档数量

}
