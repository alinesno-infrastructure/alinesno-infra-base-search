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

import java.sql.Timestamp;

/**
 * 功能名：用户身份验证和权限控制
 * 数据表：user_auth
 * 表备注：
 *
 * @TableName  表名注解，用于指定数据库表名
 * @TableField  字段注解，用于指定数据库字段名
 *
 * @author  luoxiaodong
 * @version  1.0.0
 */
@TableName("user_auth")
@Data
public class UserAuthEntity extends InfraBaseEntity {

    // 用户ID，主键
    @ColumnType(MySqlTypeConstant.BIGINT)
    @ColumnComment("用户ID，主键")
    @TableField("user_id")
    private Integer userId;

    // 创建时间
    @ColumnType(value = MySqlTypeConstant.DATETIME, length = 18)
    @ColumnComment("创建时间")
    @TableField("created_at")
    private Timestamp createdAt;
}