package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("dict")
public class Dict {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Date createTime;
    private Date updateTime;
}
