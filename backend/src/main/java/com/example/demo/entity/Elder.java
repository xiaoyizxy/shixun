package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("elder")
public class Elder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String phone;
    private String address;
    private String careLevel;
    private String nurse;
    private String status;
    private Date createTime;
    private Date updateTime;
}
