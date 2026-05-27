package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.ServiceOrderService;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Result;
import com.example.demo.entity.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ServiceOrderService orderService;

    @GetMapping("/list")
    public Result list() {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Appointment::getCreateTime);
        List<Appointment> list = appointmentService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(appointmentService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody Appointment appointment) {
        appointment.setCreateTime(new Date());
        appointment.setUpdateTime(new Date());
        if (appointment.getStatus() == null) appointment.setStatus("pending");
        appointmentService.save(appointment);
        return Result.success(appointment);
    }

    @PutMapping
    public Result update(@RequestBody Appointment appointment) {
        appointment.setUpdateTime(new Date());
        appointmentService.updateById(appointment);
        return Result.success(appointment);
    }

    /**
     * 确认预约 → 自动生成服务订单
     */
    @PutMapping("/{id}/confirm")
    public Result confirm(@PathVariable Long id, @RequestParam(required = false) BigDecimal amount) {
        Appointment appointment = appointmentService.getById(id);
        if (appointment == null) {
            return Result.error("预约不存在");
        }
        if (!"pending".equals(appointment.getStatus())) {
            return Result.error("当前预约状态不可确认");
        }

        // 计算金额：前端传入 > 服务类型默认价格
        BigDecimal finalAmount = amount != null && amount.compareTo(BigDecimal.ZERO) > 0
                ? amount
                : getServiceTypePrice(appointment.getServiceType());

        // 1. 更新预约状态
        appointment.setStatus("confirmed");
        appointment.setUpdateTime(new Date());
        appointmentService.updateById(appointment);

        // 2. 生成服务订单
        ServiceOrder order = new ServiceOrder();
        order.setAppointmentId(appointment.getId());
        order.setOrderNo(generateOrderNo());
        order.setElderName(appointment.getElderName());
        order.setService(getServiceTypeLabel(appointment.getServiceType()));
        order.setStaff(appointment.getStaffName());
        order.setDate(appointment.getAppointmentDate());
        order.setAmount(finalAmount);
        order.setStatus("pending");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderService.save(order);

        return Result.success("预约已确认，服务订单已生成", order);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        appointmentService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/recent")
    public Result recent() {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Appointment::getCreateTime)
               .last("LIMIT 10");
        List<Appointment> list = appointmentService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 生成订单编号：FW + yyyyMMddHHmm + 4位随机数
     */
    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        int seq = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "FW" + datePart + seq;
    }

    /**
     * 服务类型 → 中文名称
     */
    private String getServiceTypeLabel(String serviceType) {
        if (serviceType == null) return "未知服务";
        return switch (serviceType) {
            case "nursing" -> "上门护理";
            case "visit" -> "上门巡诊";
            case "rehab" -> "康复理疗";
            case "injection" -> "打针输液";
            case "care" -> "生活照料";
            case "other" -> "其他服务";
            default -> serviceType;
        };
    }

    /**
     * 服务类型 → 默认价格
     */
    private BigDecimal getServiceTypePrice(String serviceType) {
        if (serviceType == null) return BigDecimal.ZERO;
        return switch (serviceType) {
            case "nursing" -> new BigDecimal("150");
            case "visit" -> new BigDecimal("100");
            case "rehab" -> new BigDecimal("200");
            case "injection" -> new BigDecimal("80");
            case "care" -> new BigDecimal("120");
            default -> new BigDecimal("50");
        };
    }
}
