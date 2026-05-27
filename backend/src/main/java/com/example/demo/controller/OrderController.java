package com.example.demo.controller;

import com.example.demo.service.AppointmentService;
import com.example.demo.service.ServiceOrderService;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Result;
import com.example.demo.entity.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ServiceOrderService orderService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String status) {
        List<ServiceOrder> list = orderService.search(keyword, status);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody ServiceOrder order) {
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        if (order.getStatus() == null) order.setStatus("pending");
        orderService.save(order);
        return Result.success(order);
    }

    @PutMapping
    public Result update(@RequestBody ServiceOrder order) {
        order.setUpdateTime(new Date());
        orderService.updateById(order);
        return Result.success(order);
    }

    @PutMapping("/{id}/status")
    public Result updateStatus(@PathVariable Long id, @RequestParam String status) {
        ServiceOrder order = orderService.getById(id);
        if (order != null) {
            order.setStatus(status);
            order.setUpdateTime(new Date());
            orderService.updateById(order);

            // 同步更新关联的预约状态
            if (order.getAppointmentId() != null) {
                Appointment appointment = appointmentService.getById(order.getAppointmentId());
                if (appointment != null) {
                    appointment.setStatus(status);
                    appointment.setUpdateTime(new Date());
                    appointmentService.updateById(appointment);
                }
            }
        }
        return Result.success(order);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        orderService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/stats")
    public Result stats() {
        Map<String, Object> stats = orderService.getStats();
        return Result.success(stats);
    }
}
