package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.NursingService;

import java.util.List;

public interface NursingServiceService extends IService<NursingService> {
    List<NursingService> search(String keyword, String category);
}
