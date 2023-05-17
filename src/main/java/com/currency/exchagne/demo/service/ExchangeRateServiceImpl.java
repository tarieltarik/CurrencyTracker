package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.Currency;
import com.currency.exchagne.demo.model.ExchangeRate;
import com.currency.exchagne.demo.reposiroty.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeRateServiceImpl implements ExchangeRateService{
    @Value("${demo.mail}")
    private String toEmail;
    @Value("${demo.mail.message.subject}")
    private String mailSubject;
    private final CurrencyService currencyService;
    private final ExchangeRateRepository rateRepository;

    private final EmailService emailService;
    @Override
    public ExchangeRate saveExchangeRateFromApi(){
        List<Currency> currencies = currencyService.getBestCurrencyList();
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .dateTime(LocalDateTime.now())
                .build();

        exchangeRate = rateRepository.save(exchangeRate);

        for(Currency currency : currencies){
            currency.setExchangeRate(exchangeRate);
            currencyService.saveCurrency(currency);
        }
        exchangeRate.setCurrencies(currencies);
        emailService.sendEmail(toEmail,exchangeRate.toString(),mailSubject);
        return rateRepository.save(exchangeRate);
    }

    @Override
    public List<ExchangeRate> getExchangeRateList(){
        return rateRepository.findAll();
    }
}
