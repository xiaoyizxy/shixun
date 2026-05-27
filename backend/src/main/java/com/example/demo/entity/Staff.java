package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("staff")
public class Staff {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String title;
    private String phone;
    private String dept;
    private String schedule;
    private Double rating;
    private String cert;
    private String status;
    private Date createTime;
    private Date updateTime;
}
