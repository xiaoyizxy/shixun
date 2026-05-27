package com.example.demo.controller;

import com.example.demo.service.ElderService;
import com.example.demo.entity.Elder;
import com.example.demo.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/elder")
public class ElderController {

    @Autowired
    private ElderService elderService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String careLevel) {
        List<Elder> list = elderService.search(keyword, careLevel);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(elderService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Elder elder) {
        elder.setCreateTime(new Date());
        elder.setUpdateTime(new Date());
        if (elder.getStatus() == null) elder.setStatus("active");
        elderService.save(elder);
        return Result.success(elder);
    }

    @PutMapping
    public Result update(@RequestBody Elder elder) {
        elder.setUpdateTime(new Date());
        elderService.updateById(elder);
        return Result.success(elder);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        elderService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/stats")
    public Result stats() {
        List<Elder> all = elderService.list();
        long total = all.size();
        long active = all.stream().filter(e -> "active".equals(e.getStatus())).count();
        Result result = Result.success(null);
        result.put("total", total);
        result.put("active", active);
        return result;
    }
}
