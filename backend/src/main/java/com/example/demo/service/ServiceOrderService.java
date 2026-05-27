package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.ServiceOrder;

import java.util.List;
import java.util.Map;

public interface ServiceOrderService extends IService<ServiceOrder> {
    List<ServiceOrder> search(String keyword, String status);
    Map<String, Object> getStats();
}
