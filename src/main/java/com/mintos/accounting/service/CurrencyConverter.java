package com.mintos.accounting.service;

import com.mintos.accounting.common.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyConverter {

    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        return amount;
    }
}
