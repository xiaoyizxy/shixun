package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@TableName("service_order")
public class ServiceOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long appointmentId;
    private String elderName;
    private String service;
    private String staff;
    private String date;
    private BigDecimal amount;
    private String status;
    private Date createTime;
    private Date updateTime;
}
