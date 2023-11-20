package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;

import java.math.BigDecimal;
import java.util.Set;

import static java.util.Optional.ofNullable;

public interface CurrencyConverter {

    BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo);

    Set<Currency> getSupportedCurrencies();

}
