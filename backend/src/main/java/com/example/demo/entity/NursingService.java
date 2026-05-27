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
@TableName("nursing_service")
public class NursingService {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private String unit;
    private String duration;
    private String description;
    private String tags;
    private Date createTime;
    private Date updateTime;
}
