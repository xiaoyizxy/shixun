package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.ServiceOrderMapper;
import com.example.demo.service.ServiceOrderService;
import com.example.demo.entity.ServiceOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceOrderServiceImpl extends ServiceImpl<ServiceOrderMapper, ServiceOrder> implements ServiceOrderService {

    @Override
    public List<ServiceOrder> search(String keyword, String status) {
        LambdaQueryWrapper<ServiceOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(ServiceOrder::getOrderNo, keyword)
                    .or().like(ServiceOrder::getElderName, keyword)
                    .or().like(ServiceOrder::getService, keyword));
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ServiceOrder::getStatus, status);
        }
        wrapper.orderByDesc(ServiceOrder::getCreateTime);
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        List<ServiceOrder> all = list();
        long totalCount = all.size();
        long completedCount = all.stream().filter(o -> "completed".equals(o.getStatus())).count();
        long pendingCount = all.stream().filter(o -> "pending".equals(o.getStatus())).count();

        BigDecimal revenue = all.stream()
                .filter(o -> "completed".equals(o.getStatus()) || "in-progress".equals(o.getStatus()))
                .map(ServiceOrder::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", totalCount);
        stats.put("completed", completedCount);
        stats.put("pending", pendingCount);
        stats.put("revenue", revenue);
        return stats;
    }
}
