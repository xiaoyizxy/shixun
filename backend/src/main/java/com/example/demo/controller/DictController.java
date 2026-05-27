package com.example.demo.controller;

import com.example.demo.service.DictService;
import com.example.demo.entity.Dict;
import com.example.demo.entity.DictItem;
import com.example.demo.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/list")
    public Result list() {
        List<Map<String, Object>> list = dictService.getAllWithItems();
        return Result.success(list);
    }

    @GetMapping("/{code}/items")
    public Result items(@PathVariable String code) {
        List<DictItem> items = dictService.getItemsByDictCode(code);
        return Result.success(items);
    }

    @PostMapping
    public Result add(@RequestBody Dict dict) {
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        dictService.save(dict);
        return Result.success(dict);
    }

    @PutMapping
    public Result update(@RequestBody Dict dict) {
        dict.setUpdateTime(new Date());
        dictService.updateById(dict);
        return Result.success(dict);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        dictService.removeById(id);
        return Result.success("删除成功", null);
    }
}
