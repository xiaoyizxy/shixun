package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Dict;
import com.example.demo.entity.DictItem;

import java.util.List;
import java.util.Map;

public interface DictService extends IService<Dict> {
    List<Map<String, Object>> getAllWithItems();
    List<DictItem> getItemsByDictCode(String code);
}
