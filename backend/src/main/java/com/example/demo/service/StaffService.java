package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Staff;

import java.util.List;

public interface StaffService extends IService<Staff> {
    List<Staff> search(String keyword);
}
