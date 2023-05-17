package com.currency.exchagne.demo.reposiroty;

import com.currency.exchagne.demo.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
}
