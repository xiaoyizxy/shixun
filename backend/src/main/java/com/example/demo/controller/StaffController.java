package com.example.demo.controller;

import com.example.demo.service.StaffService;
import com.example.demo.entity.Result;
import com.example.demo.entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String keyword) {
        List<Staff> list = staffService.search(keyword);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(staffService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Staff staff) {
        staff.setCreateTime(new Date());
        staff.setUpdateTime(new Date());
        if (staff.getStatus() == null) staff.setStatus("active");
        if (staff.getRating() == null) staff.setRating(4.0);
        staffService.save(staff);
        return Result.success(staff);
    }

    @PutMapping
    public Result update(@RequestBody Staff staff) {
        staff.setUpdateTime(new Date());
        staffService.updateById(staff);
        return Result.success(staff);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        staffService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/stats")
    public Result stats() {
        List<Staff> all = staffService.list();
        long total = all.size();
        long active = all.stream().filter(s -> "active".equals(s.getStatus())).count();
        long dayShift = all.stream().filter(s -> "白班".equals(s.getSchedule())).count();
        long nightShift = all.stream().filter(s -> "夜班".equals(s.getSchedule())).count();
        Result result = Result.success(null);
        result.put("total", total);
        result.put("active", active);
        result.put("dayShift", dayShift);
        result.put("nightShift", nightShift);
        return result;
    }
}
