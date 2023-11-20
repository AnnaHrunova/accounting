package com.mintos.accounting.service.converter.config;

import com.mintos.accounting.common.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ConverterProperties {

    @NotBlank
    private String path;

    @NotBlank
    private String query;

    @NotNull
    private Map<Currency, Map<Currency, BigDecimal>> fallbackRates;
}
