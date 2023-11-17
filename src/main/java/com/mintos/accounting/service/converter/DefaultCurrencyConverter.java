package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.ExternalApiCallException;
import com.mintos.accounting.service.converter.config.ExchangeProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Set;

import static java.util.Collections.emptySet;

@Component
@AllArgsConstructor
@Profile("!mock")
@EnableCaching
@Slf4j
public class DefaultCurrencyConverter implements CurrencyConverter {

    private final ExchangeProperties exchangeProperties;
    private final RestTemplate restTemplate;

    @Override
    public BigDecimal convert(BigDecimal amount, Currency currencyFrom, Currency currencyTo) {
        val converter = exchangeProperties.getConvert();
        val baseUrl = String.format(exchangeProperties.getBaseUrl(), converter.getPath(), exchangeProperties.getApiKey());
        val query = String.format(converter.getQuery(), currencyFrom, currencyTo, amount);

        try {
            val response = restTemplate.getForObject(baseUrl + query, ExchangeResponse.class);
            return response != null ? response.getResult() : null;
        } catch (ExternalApiCallException ex) {
            log.error("Error while currency conversion by external API.");
            return null;
        }
    }

    @Cacheable("supportedCurrencies")
    @Override
    public Set<String> getSupportedCurrencies() {
        val currencies = exchangeProperties.getCurrency();
        val url = String.format(exchangeProperties.getBaseUrl(), currencies.getPath(), exchangeProperties.getApiKey());
        try {
            val response = restTemplate.getForObject(url, ExchangeResponse.class);
            return response != null ? response.getCurrencies().keySet() : emptySet();
        } catch (ExternalApiCallException e) {
            log.error("Error while fetching supported currencies from external API.");
            log.warn("Returning fallback currencies list.");
            return currencies.getFallback();
        }
    }

    @CacheEvict(value = "supportedCurrencies", allEntries = true)
    @Scheduled(fixedRateString = "600000")
    public void emptySupportedCurrenciesCache() {
        log.info("Empty supportedCurrencies cache");
    }
}