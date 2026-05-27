package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("bed")
public class Bed {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String bedNo;
    private String elderName;
    private Integer age;
    private String careLevel;
    private String nurse;
    private String checkInDate;
    private String status;
    private Date createTime;
    private Date updateTime;
}
