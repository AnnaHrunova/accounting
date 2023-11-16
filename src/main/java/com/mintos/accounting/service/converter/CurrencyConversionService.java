package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
@EnableConfigurationProperties(ConverterProperties.class)
public class CurrencyConversionService {

    private final CurrencyConverter currencyConverter;

    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        return currencyConverter.convert(amount, currencyFrom, currencyTo);
    }
}
