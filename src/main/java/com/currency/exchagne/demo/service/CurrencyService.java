package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.Currency;
import com.currency.exchagne.demo.model.ExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CurrencyService {

    List<Currency> getBestCurrencyList();

    void saveCurrency(Currency currency);

}
