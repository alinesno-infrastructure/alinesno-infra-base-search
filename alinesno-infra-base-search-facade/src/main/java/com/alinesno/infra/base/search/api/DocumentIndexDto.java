package com.alinesno.infra.base.search.api;

import com.alinesno.infra.base.search.enums.IndexTypeEnums;
import com.alinesno.infra.common.facade.base.BaseDto;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名：文档索引
 * 数据表：document_index
 * 表备注：用于存储Elasticsearch索引的相关信息，便于后续的数据检索和分析。
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentIndexDto extends BaseDto {

    private String indexBase;
    private String indexBaseDesc ;
    private String fieldName;
    private String indexType = IndexTypeEnums.DAILY.getValue() ;
    private String docId;
    private int shardNum;
    private int replicaNum;
    private long docCount;
    private String alias;
    private long storageSize;

}