package com.currency.exchagne.demo.controller;

import com.currency.exchagne.demo.model.CronExpression;
import com.currency.exchagne.demo.model.ExchangeRate;
import com.currency.exchagne.demo.service.CronExpressionService;
import com.currency.exchagne.demo.service.EmailService;
import com.currency.exchagne.demo.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CronExpressionService cronService;
    private final ExchangeRateService rateService;
    private final EmailService emailService;

    @GetMapping("/")
    public String getCurrencies(Model model) {
        List<ExchangeRate> exchangeRates = rateService.getExchangeRateList();
        model.addAttribute("exchangeRates", exchangeRates);
        return "currencyList";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testList(){
        rateService.saveExchangeRateFromApi();
        return "Currency load successfully!";
    }

    @GetMapping("/send/{to}/{body}/{subject}")
    public void sendMessage(@PathVariable(name = "to") String toEmail,
                            @PathVariable(name = "body") String body,
                            @PathVariable(name = "subject") String subject){
        emailService.sendEmail(toEmail,body,subject);
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        CronExpression cronExpression = cronService.getFirstCronExpression();
        model.addAttribute("cron",cronExpression);
        return "schedule";
    }

    @PostMapping("/cron/save")
    public String saveCronExpression(@ModelAttribute("cron") CronExpression cronExpression){
        cronService.saveCronExpression(cronExpression);
        return "redirect:/";
    }
}
