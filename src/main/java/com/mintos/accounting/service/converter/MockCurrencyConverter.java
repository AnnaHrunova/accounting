package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

@Component
@AllArgsConstructor
@Profile("mock")
public class MockCurrencyConverter implements CurrencyConverter {

    private final ConverterProperties converterProperties;

    @Override
    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        return toMoney(amount.multiply(converterProperties.getMockRate()));
    }
}
