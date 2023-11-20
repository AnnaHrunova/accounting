package com.mintos.accounting.service.converter.config;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RateGroup {
    private Currency from;
    private Currency to;
}