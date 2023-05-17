package com.currency.exchagne.demo.reposiroty;

import com.currency.exchagne.demo.model.CronExpression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronExpressionRepository extends JpaRepository<CronExpression,Long> {
}
