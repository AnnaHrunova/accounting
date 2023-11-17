package com.mintos.accounting.service.converter.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CurrencyProperties {

    @NotBlank
    private String path;

    @NotNull
    private Set<String> fallback;
}
