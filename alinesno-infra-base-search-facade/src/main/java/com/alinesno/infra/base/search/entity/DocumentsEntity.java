package com.alinesno.infra.base.search.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;


/**
 * 功能名：文档
 * 数据表：documents
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */

@EqualsAndHashCode(callSuper = true)
@TableName("documents")
@Data
public class DocumentsEntity extends InfraBaseEntity {

    // 文档ID
    @ColumnType(MySqlTypeConstant.BIGINT)
    @ColumnComment("文档ID，主键")
    @TableField("document_id")
    private Integer documentId;

    // 文档标题
    @ColumnType(length = 255)
    @ColumnComment("文档标题")
    @TableField("title")
    private String title;

    // 文档内容
    @ColumnType(MySqlTypeConstant.MEDIUMTEXT)
    @ColumnComment("文档内容")
    @TableField("content")
    private String content;

    // 创建时间
    @ColumnType(value = MySqlTypeConstant.DATETIME, length = 18)
    @ColumnComment("创建时间")
    @TableField("created_at")
    private Timestamp createdAt;

    // 省略getter和setter方法
}