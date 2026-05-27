package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.NursingServiceMapper;
import com.example.demo.service.NursingServiceService;
import com.example.demo.entity.NursingService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class NursingServiceServiceImpl extends ServiceImpl<NursingServiceMapper, NursingService> implements NursingServiceService {

    @Override
    public List<NursingService> search(String keyword, String category) {
        LambdaQueryWrapper<NursingService> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(NursingService::getName, keyword)
                    .or().like(NursingService::getDescription, keyword));
        }
        if (StringUtils.hasText(category) && !"all".equals(category)) {
            wrapper.eq(NursingService::getCategory, category);
        }
        return list(wrapper);
    }
}
