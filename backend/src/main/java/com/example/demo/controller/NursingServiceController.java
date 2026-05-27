package com.example.demo.controller;

import com.example.demo.service.NursingServiceService;
import com.example.demo.entity.NursingService;
import com.example.demo.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/nursing-service")
public class NursingServiceController {

    @Autowired
    private NursingServiceService nursingServiceService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String category) {
        List<NursingService> list = nursingServiceService.search(keyword, category);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(nursingServiceService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody NursingService service) {
        service.setCreateTime(new Date());
        service.setUpdateTime(new Date());
        nursingServiceService.save(service);
        return Result.success(service);
    }

    @PutMapping
    public Result update(@RequestBody NursingService service) {
        service.setUpdateTime(new Date());
        nursingServiceService.updateById(service);
        return Result.success(service);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        nursingServiceService.removeById(id);
        return Result.success("删除成功", null);
    }
}
