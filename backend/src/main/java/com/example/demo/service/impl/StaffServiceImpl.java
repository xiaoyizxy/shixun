package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.StaffService;
import com.example.demo.entity.Staff;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Override
    public List<Staff> search(String keyword) {
        LambdaQueryWrapper<Staff> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Staff::getName, keyword)
                    .or().like(Staff::getDept, keyword)
                    .or().like(Staff::getTitle, keyword));
        }
        wrapper.orderByDesc(Staff::getCreateTime);
        return list(wrapper);
    }
}
