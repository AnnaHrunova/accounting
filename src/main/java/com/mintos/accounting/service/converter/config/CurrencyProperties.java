package com.mintos.accounting.service.converter.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CurrencyProperties {

    @NotBlank
    private String path;

}
