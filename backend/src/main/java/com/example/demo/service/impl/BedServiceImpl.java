package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.BedMapper;
import com.example.demo.service.BedService;
import com.example.demo.entity.Bed;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed> implements BedService {

    @Override
    public List<Bed> search(String status) {
        LambdaQueryWrapper<Bed> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) {
            wrapper.eq(Bed::getStatus, status);
        }
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        List<Bed> all = list();
        long total = all.size();
        long occupied = all.stream().filter(b -> "occupied".equals(b.getStatus())).count();
        long vacant = all.stream().filter(b -> "vacant".equals(b.getStatus())).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("occupied", occupied);
        stats.put("vacant", vacant);
        stats.put("pending", 0L);
        return stats;
    }
}
