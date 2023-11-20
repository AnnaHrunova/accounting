package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;

import java.math.BigDecimal;
import java.util.Set;

public interface CurrencyConverter {

    BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo);

    Set<Currency> getSupportedCurrencies();

}
