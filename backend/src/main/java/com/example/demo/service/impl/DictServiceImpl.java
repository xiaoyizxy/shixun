package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.DictItemMapper;
import com.example.demo.mapper.DictMapper;
import com.example.demo.service.DictService;
import com.example.demo.entity.Dict;
import com.example.demo.entity.DictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public List<Map<String, Object>> getAllWithItems() {
        List<Dict> dicts = list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Dict dict : dicts) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("code", dict.getCode());
            map.put("name", dict.getName());
            LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DictItem::getDictId, dict.getId());
            wrapper.orderByAsc(DictItem::getSort);
            map.put("items", dictItemMapper.selectList(wrapper));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<DictItem> getItemsByDictCode(String code) {
        LambdaQueryWrapper<Dict> dictWrapper = new LambdaQueryWrapper<>();
        dictWrapper.eq(Dict::getCode, code);
        Dict dict = getOne(dictWrapper);
        if (dict == null) return Collections.emptyList();

        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictItem::getDictId, dict.getId());
        wrapper.orderByAsc(DictItem::getSort);
        return dictItemMapper.selectList(wrapper);
    }
}
