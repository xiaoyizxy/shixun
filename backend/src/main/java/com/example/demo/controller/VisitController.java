package com.example.demo.controller;

import com.example.demo.service.VisitService;
import com.example.demo.entity.Result;
import com.example.demo.entity.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/visit")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String doctor) {
        List<Visit> list = visitService.search(doctor);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(visitService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Visit visit) {
        visit.setCreateTime(new Date());
        visit.setUpdateTime(new Date());
        if (visit.getStatus() == null) visit.setStatus("upcoming");
        visitService.save(visit);
        return Result.success(visit);
    }

    @PutMapping
    public Result update(@RequestBody Visit visit) {
        visit.setUpdateTime(new Date());
        visitService.updateById(visit);
        return Result.success(visit);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        visitService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/today")
    public Result today() {
        List<Visit> all = visitService.list();
        long todayCount = all.stream()
                .filter(v -> "upcoming".equals(v.getStatus()) || "in-progress".equals(v.getStatus()))
                .count();
        Result result = Result.success(null);
        result.put("todayVisits", todayCount);
        return result;
    }
}
