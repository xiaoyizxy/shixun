package com.example.demo.controller;

import com.example.demo.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @GetMapping("/dashboard")
    public Result dashboard() {
        Map<String, Object> data = new LinkedHashMap<>();

        // KPI 数据
        List<Map<String, Object>> kpiCards = new ArrayList<>();
        kpiCards.add(createKpi("本月服务人次", 156, "+12%", "up"));
        kpiCards.add(createKpi("在册老人数", 48, "+3", "up"));
        kpiCards.add(createKpi("在岗护理人员", 22, "持平", "stable"));
        kpiCards.add(createKpi("患者满意度", "98.5%", "+0.3%", "up"));
        data.put("kpiCards", kpiCards);

        // 服务分布
        List<Map<String, Object>> serviceData = new ArrayList<>();
        serviceData.add(createBar("压疮护理", 32, "#1890ff"));
        serviceData.add(createBar("静脉输液", 28, "#52c41a"));
        serviceData.add(createBar("康复训练", 24, "#faad14"));
        serviceData.add(createBar("生活护理", 22, "#ff7875"));
        serviceData.add(createBar("中医按摩", 18, "#722ed1"));
        serviceData.add(createBar("PICC维护", 14, "#13c2c2"));
        serviceData.add(createBar("术后换药", 10, "#eb2f96"));
        serviceData.add(createBar("其他服务", 8, "#a0d911"));
        data.put("serviceData", serviceData);

        // 周趋势
        List<Map<String, Object>> weeklyData = new ArrayList<>();
        weeklyData.add(createDay("周一", 8));
        weeklyData.add(createDay("周二", 12));
        weeklyData.add(createDay("周三", 10));
        weeklyData.add(createDay("周四", 15));
        weeklyData.add(createDay("周五", 13));
        weeklyData.add(createDay("周六", 7));
        weeklyData.add(createDay("周日", 5));
        data.put("weeklyData", weeklyData);

        // 护理质量
        Map<String, Object> quality = new LinkedHashMap<>();
        quality.put("punctuality", 96);
        quality.put("standardization", 92);
        quality.put("satisfaction", 98.5);
        data.put("quality", quality);

        return Result.success(data);
    }

    private Map<String, Object> createKpi(String label, Object value, String change, String trend) {
        Map<String, Object> kpi = new LinkedHashMap<>();
        kpi.put("label", label);
        kpi.put("value", value);
        kpi.put("change", change);
        kpi.put("trend", trend);
        return kpi;
    }

    private Map<String, Object> createBar(String name, int count, String color) {
        Map<String, Object> bar = new LinkedHashMap<>();
        bar.put("name", name);
        bar.put("count", count);
        bar.put("color", color);
        return bar;
    }

    private Map<String, Object> createDay(String day, int count) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("day", day);
        d.put("count", count);
        return d;
    }
}
