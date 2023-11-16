package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;

import java.math.BigDecimal;

public interface CurrencyConverter {

    BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo);

}
