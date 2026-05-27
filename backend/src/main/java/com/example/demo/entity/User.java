package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Integer age;
    private String email;
    @TableField("real_name")
    private String realname;
    private String phone;
    private String role;
    private String status;
    private Date createTime;
    private Date updateTime;

    @TableField(value = "is_deleted")
    private Integer isDeleted;
}