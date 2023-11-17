package com.mintos.accounting.service.converter.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConverterProperties {

    @NotBlank
    private String path;

    @NotBlank
    private String query;
}
