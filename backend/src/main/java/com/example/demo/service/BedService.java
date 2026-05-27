package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Bed;

import java.util.List;
import java.util.Map;

public interface BedService extends IService<Bed> {
    List<Bed> search(String status);
    Map<String, Object> getStats();
}
