package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("visit")
public class Visit {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private Integer age;
    private String symptom;
    private String doctor;
    private String visitDate;
    private String timeSlot;
    private String status;
    private String diagnosis;
    private Date createTime;
    private Date updateTime;
}
