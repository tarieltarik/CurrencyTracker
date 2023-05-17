package com.currency.exchagne.demo.schedule;

import com.currency.exchagne.demo.service.CronExpressionService;
import com.currency.exchagne.demo.service.ExchangeRateService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@EnableScheduling
@Data
@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulingConfiguration implements SchedulingConfigurer {

    private final CronExpressionService cronExpressionService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                log.info("Current time： {}", LocalDateTime.now());
                exchangeRateService.saveExchangeRateFromApi();
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger cronTrigger = new CronTrigger(cronExpressionService.getCronExpression());
                Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
                return nextExecutionTime;
            }
        });
    }
}
