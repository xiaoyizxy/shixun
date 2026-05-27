package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("dict_item")
public class DictItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dictId;
    private String label;
    private String value;
    private Integer sort;
    private String status;
    private Date createTime;
    private Date updateTime;
}
