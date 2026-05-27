package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Visit;

import java.util.List;

public interface VisitService extends IService<Visit> {
    List<Visit> search(String doctor);
}
