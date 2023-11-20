package com.mintos.accounting.service.converter;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.config.BaseIntegrationTest;
import com.mintos.accounting.exceptions.ExternalApiCallException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DefaultCurrencyConverterTest extends BaseIntegrationTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    DefaultCurrencyConverter defaultCurrencyConverter;

    @Test
    void shouldUseFallback_ExternalApiException() {
        when(restTemplate.getForObject(any(String.class), eq(ExchangeResponse.class))).thenThrow(ExternalApiCallException.class);
        val amount = new BigDecimal("100");
        val result = defaultCurrencyConverter.convert(amount, Currency.EUR, Currency.GBP);
        assertThat(result).isEqualTo(toMoney(amount.multiply(new BigDecimal("0.8800000"))));
    }

    @Test
    void shouldUseFallback_CurrencyFromNotSupported() {
        when(restTemplate.getForObject(any(String.class), eq(ExchangeResponse.class))).thenThrow(ExternalApiCallException.class);
        val amount = new BigDecimal("100");
        val result = defaultCurrencyConverter.convert(amount, Currency.QQQ, Currency.GBP);
        assertThat(result).isNull();
    }

    @Test
    void shouldUseFallback_CurrencyToNotSupported() {
        when(restTemplate.getForObject(any(String.class), eq(ExchangeResponse.class))).thenThrow(ExternalApiCallException.class);
        val amount = new BigDecimal("100");
        val result = defaultCurrencyConverter.convert(amount, Currency.EUR, Currency.QQQ);
        assertThat(result).isNull();
    }
}