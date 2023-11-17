package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountData {
    private UUID id;
    private Currency currency;
    private BigDecimal balance;
}
