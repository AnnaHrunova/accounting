package com.mintos.accounting.service.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeResponse {
    private BigDecimal result;
    private Map<String, String> currencies;
}
