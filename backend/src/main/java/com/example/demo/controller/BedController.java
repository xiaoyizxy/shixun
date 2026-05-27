package com.example.demo.controller;

import com.example.demo.service.BedService;
import com.example.demo.entity.Bed;
import com.example.demo.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bed")
public class BedController {

    @Autowired
    private BedService bedService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String status) {
        List<Bed> list = bedService.search(status);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(bedService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Bed bed) {
        bed.setCreateTime(new Date());
        bed.setUpdateTime(new Date());
        if (bed.getStatus() == null) bed.setStatus("vacant");
        bedService.save(bed);
        return Result.success(bed);
    }

    @PutMapping
    public Result update(@RequestBody Bed bed) {
        bed.setUpdateTime(new Date());
        bedService.updateById(bed);
        return Result.success(bed);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        bedService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/stats")
    public Result stats() {
        Map<String, Object> stats = bedService.getStats();
        return Result.success(stats);
    }
}
