package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("appointment")
public class Appointment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private String elderPhone;
    private Integer elderAge;
    private String elderAddress;
    private String serviceType;
    private String appointmentDate;
    private String appointmentTime;
    private String staffName;
    private String remark;
    private String status;
    private Date createTime;
    private Date updateTime;
}
