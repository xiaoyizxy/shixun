package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.ElderMapper;
import com.example.demo.service.ElderService;
import com.example.demo.entity.Elder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements ElderService {

    @Override
    public List<Elder> search(String keyword, String careLevel) {
        LambdaQueryWrapper<Elder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Elder::getName, keyword).or().like(Elder::getPhone, keyword));
        }
        if (StringUtils.hasText(careLevel)) {
            wrapper.eq(Elder::getCareLevel, careLevel);
        }
        wrapper.orderByDesc(Elder::getCreateTime);
        return list(wrapper);
    }
}
