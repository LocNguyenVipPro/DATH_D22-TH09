package com.example.sm.minh.eshop.controllers;


import com.example.sm.minh.eshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatisticsController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/statistic")
    public String SpendingStatistics(Model model) {
        List<Object[]> data  = orderService.getTotalAmountByMonthInYear();
        model.addAttribute("statistic_data", data);

        return "statistics/statistic";
    }
}
