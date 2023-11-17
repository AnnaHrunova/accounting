package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.service.converter.config.ExchangeProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@AllArgsConstructor
@EnableConfigurationProperties(ExchangeProperties.class)
public class CurrencyExchangeService {

    private final CurrencyConverter currencyConverter;

    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        return currencyConverter.convert(amount, currencyFrom, currencyTo);
    }

    public Set<String> getSupportedCurrencies() {
        return currencyConverter.getSupportedCurrencies();
    }
}
