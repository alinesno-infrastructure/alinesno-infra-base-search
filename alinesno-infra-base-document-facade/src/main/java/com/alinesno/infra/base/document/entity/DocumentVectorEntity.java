package com.alinesno.infra.base.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 功能名：文档向量
 * 数据表：document_vectors
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */

@TableName("document_vectors")
@Data
public class DocumentVectorEntity extends InfraBaseEntity {

    // 文档ID
    @ColumnType(MySqlTypeConstant.BIGINT)
    @ColumnComment("对应文档ID，外键关联documents表")
    @TableField("document_id")
    private Integer documentId;

    // 向量数据
    @ColumnType(length = 255)
    @ColumnComment("存储向量数据的字段")
    @TableField("vector_data")
    private String vectorData;

    // 省略getter和setter方法
}