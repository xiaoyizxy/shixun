package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.service.VisitService;
import com.example.demo.entity.Visit;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, Visit> implements VisitService {

    @Override
    public List<Visit> search(String doctor) {
        LambdaQueryWrapper<Visit> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(doctor) && !"全部医生".equals(doctor)) {
            wrapper.eq(Visit::getDoctor, doctor);
        }
        wrapper.orderByDesc(Visit::getVisitDate);
        return list(wrapper);
    }
}
