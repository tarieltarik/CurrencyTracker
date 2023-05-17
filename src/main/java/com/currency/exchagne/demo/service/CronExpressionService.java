package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.CronExpression;
import com.currency.exchagne.demo.reposiroty.CronExpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CronExpressionService {

    private final CronExpressionRepository cronRepository;

    @PostConstruct
    private void defaultCronValue(){
        CronExpression cronExpression = CronExpression.builder()
                .hour("*")
                .min("*")
                .second("*/10")
                .build();
        cronRepository.save(cronExpression);
    }

    public void saveCronExpression(CronExpression expression){
        if(cronRepository.findAll().isEmpty()){
            cronRepository.save(expression);
        }else{
            CronExpression cronExpression = cronRepository.findAll().get(0);
            cronExpression.setHour(expression.getHour());
            cronExpression.setMin(expression.getMin());
            cronExpression.setSecond(expression.getSecond());
            cronRepository.save(cronExpression);
        }
    }

    public CronExpression getFirstCronExpression(){
        return cronRepository.findAll().get(0);
    }

    public String getCronExpression() {
        CronExpression cronExpression = cronRepository.findAll().get(0);
        String cron = String.format("%s %s %s * * *",cronExpression.getSecond()
                ,cronExpression.getMin(),cronExpression.getHour());
        return cron;
    }
}
