package com.currency.exchagne.demo.reposiroty;

import com.currency.exchagne.demo.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {

    @Query("SELECT er FROM ExchangeRate er JOIN FETCH er.currencies")
    List<ExchangeRate> getAllExchangeRate();

}
