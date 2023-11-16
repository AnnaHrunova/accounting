package com.mintos.accounting.service.converter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Data
@ConfigurationProperties(prefix = "exchange")
public class ConverterProperties {

    @NotBlank
    private String host;

    @NotBlank
    private String converter;

    @NotBlank
    private String list;

    @NotBlank
    private String apiKey;

    @NotNull
    private BigDecimal mockRate;
}
