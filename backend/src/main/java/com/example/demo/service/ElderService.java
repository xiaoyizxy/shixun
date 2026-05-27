package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Elder;

import java.util.List;

public interface ElderService extends IService<Elder> {
    List<Elder> search(String keyword, String careLevel);
}
