package com.mintos.accounting.api.model;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataResponse {

    private UUID accountId;
    private BigDecimal balance;
    private Currency currency;
}
