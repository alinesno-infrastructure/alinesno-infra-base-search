package com.alinesno.infra.base.search.entity;

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
@TableName("document_index")
@Data
public class DocumentIndexEntity extends InfraBaseEntity {

    /**
     * 索引基础名称
     * 用于指定搜索操作所属的索引基础
     */
    @TableField(value = "index_base")
    @ColumnComment("索引基础名称")
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String indexBase;

    /**
     * 字段名称
     * 用于标识文档中的字段
     */
    @TableField(value = "field_name")
    @ColumnComment("字段名称")
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String fieldName;

    /**
     * 索引类型
     * 用于指定搜索操作的具体类型
     */
    @TableField(value = "index_type")
    @ColumnComment("索引类型")
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String indexType;

    /**
     * 文档ID
     * Elasticsearch中的唯一标识符
     */
    @TableField(value = "doc_id")
    @ColumnComment("文档ID")
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String docId;

    /**
     * 分片数
     * Elasticsearch索引的分片数量
     */
    @TableField(value = "shard_num")
    @ColumnComment("分片数")
    @ColumnType(value = MySqlTypeConstant.INT)
    private int shardNum;

    /**
     * 副本数
     * Elasticsearch索引的副本数量
     */
    @TableField(value = "replica_num")
    @ColumnComment("副本数")
    @ColumnType(value = MySqlTypeConstant.INT)
    private int replicaNum;

    /**
     * 文档数量
     * 索引中的文档总数
     */
    @TableField(value = "doc_count")
    @ColumnComment("文档数量")
    @ColumnType(value = MySqlTypeConstant.BIGINT)
    private long docCount;

    /**
     * 别名
     * 索引的别名
     */
    @TableField(value = "alias")
    @ColumnComment("别名")
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String alias;

    /**
     * 存储数据量
     * 索引占用的存储空间大小
     */
    @TableField(value = "storage_size")
    @ColumnComment("存储数据量")
    @ColumnType(value = MySqlTypeConstant.BIGINT)
    private long storageSize;


}