package com.mintos.accounting.service.converter.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Data
@ConfigurationProperties(prefix = "exchange")
public class ExchangeProperties {

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String apiKey;

    @NotNull
    private ConverterProperties convert;

    @NotNull
    private CurrencyProperties currency;

    @NotNull
    private BigDecimal mockRate;
}
