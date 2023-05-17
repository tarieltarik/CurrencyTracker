package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.Currency;
import com.currency.exchagne.demo.model.ExchangeRate;
import com.currency.exchagne.demo.reposiroty.CurrencyRepository;
import com.currency.exchagne.demo.reposiroty.ExchangeRateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CurrencyServiceImpl implements CurrencyService{

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Value("${currency.from}")
    private String currencyFrom;
    @Value("${currency.to}")
    private String currencyTo;
    @Value("${fastforex.url}")
    private String fastforexUrl;
    @Value("${fastforex.name}")
    private String fastForexName;
    @Value("${fixer.api.url}")
    private String fixerApiUrl;
    @Value("${fixer.api.name}")
    private String fixerName;
    @Value("${fixer.api.key}")
    private String fixerKey;
    @Value("${rate.api.name}")
    private String rateName;
    @Value("${rate.api.url}")
    private String rateApiUrl;
    @Value("${rate.api.key}")
    private String rateKey;

    @Override
    public void saveCurrency(Currency currency){
        currencyRepository.save(currency);
    }
    // Метод для формирования объекта ExchangeRate
    @Override
    public List<Currency> getBestCurrencyList(){
        List<Currency> currencyList = new ArrayList<>();

        //currency rate from fast forex
        setCurrencyFromFastForex(currencyList);

        //currency rate from FixerApi
        setCurrencyFromFixerApi(currencyList);

        //currency rate from ExchangerRate
        setCurrencyFromExchangerRate(currencyList);

        currencyList = currencyList.stream()
                .collect(Collectors.groupingBy(Currency::getCode,
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparingDouble(Currency::getExchangeRateValue)),
                                Optional::get
                        )))
                .values()
                .stream()
                .collect(Collectors.toList());

        return currencyList;
    }

    private String getResponseFromExchangeRates(){
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT,"application/json")
                .defaultHeader("apikey",rateKey)
                .build();

        Mono<String> mono = webClient.get()
                .uri(rateApiUrl)
                .retrieve()
                .bodyToMono(String.class);

        return mono.block();
    }

    private void setCurrencyFromExchangerRate(List<Currency> currencyList){
        String response = getResponseFromExchangeRates();
        Currency tempCurrency;

        if(!response.isEmpty()){
            String[] currencyArray = currencyTo.split(",");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            JsonNode arrayJson = null;

            try {
                jsonNode = objectMapper.readTree(response);
                arrayJson = jsonNode.get("rates");
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
            }

            for(int i = 0; i < currencyArray.length; i++){
                tempCurrency = Currency.builder()
                        .code(currencyArray[i])
                        .exchangerName(rateName)
                        .exchangeRateValue(Double.parseDouble(arrayJson.get(currencyArray[i]).asText()))
                        .build();

                currencyList.add(tempCurrency);
            }
        }
    }
    private String getResponseFromFixerApi(){
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT,"application/json")
                .defaultHeader("apikey",fixerKey)
                .build();

        Mono<String> mono = webClient.get()
                .uri(fixerApiUrl)
                .retrieve()
                .bodyToMono(String.class);

        return mono.block();
    }

    private void setCurrencyFromFixerApi(List<Currency> currencyList){
        String response = getResponseFromFixerApi();
        Currency tempCurrency;

        if(!response.isEmpty()){
            String[] currencyArray = currencyTo.split(",");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            JsonNode arrayJson = null;

            try {
                jsonNode = objectMapper.readTree(response);
                arrayJson = jsonNode.get("rates");
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
            }

            for(int i = 0; i < currencyArray.length; i++){
                tempCurrency = Currency.builder()
                        .code(currencyArray[i])
                        .exchangerName(fixerName)
                        .exchangeRateValue(Double.parseDouble(arrayJson.get(currencyArray[i]).asText()))
                        .build();

                currencyList.add(tempCurrency);
            }
        }
    }
    private String getResponseFromFastForex(){
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT,"application/json")
                .build();

        Mono<String> mono = webClient.get()
                .uri(fastforexUrl)
                .retrieve()
                .bodyToMono(String.class);

        return mono.block();
    }

    private void setCurrencyFromFastForex(List<Currency> currencyList){
        String response = getResponseFromFastForex();
        Currency tempCurrency;

        if(!response.isEmpty()){
            String[] currencyArray = currencyTo.split(",");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            JsonNode arrayJson = null;

            try {
                jsonNode = objectMapper.readTree(response);
                arrayJson = jsonNode.get("results");
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
            }

            for(int i = 0; i < currencyArray.length; i++){
                tempCurrency = Currency.builder()
                        .code(currencyArray[i])
                        .exchangerName(fastForexName)
                        .exchangeRateValue(Double.parseDouble(arrayJson.get(currencyArray[i]).asText()))
                        .build();

                currencyList.add(tempCurrency);
            }
        }
    }
}
