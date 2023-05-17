package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {

    ExchangeRate saveExchangeRateFromApi();

    List<ExchangeRate> getExchangeRateList();
}
