package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
@Profile("!mock")
public class DefaultCurrencyConverter implements CurrencyConverter {

    private final ConverterProperties converterProperties;
    private final RestTemplate restTemplate;

    @Override
    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        val listPath = String.format(converterProperties.getList(), converterProperties.getApiKey());
        val listUrl = converterProperties.getHost() + listPath;
        val supportedCurrencies = restTemplate.getForObject(listUrl, ExchangeResponse.class)
                .getCurrencies().keySet();
        if (supportedCurrencies.contains(currencyFrom.name()) && supportedCurrencies.contains(currencyTo.name())) {
            val converterPath = String.format(converterProperties.getConverter(), converterProperties.getApiKey(), currencyFrom, currencyTo, amount);
            val converterUrl = converterProperties.getHost() + converterPath;
            val response = restTemplate.getForObject(converterUrl, ExchangeResponse.class);
            return response.getResult();
        } else {
            return null;
        }
    }
}
