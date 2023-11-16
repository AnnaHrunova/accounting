package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionData {

    private String requestId;

    private Currency currency;

    private BigDecimal amount;

    private BigDecimal convertedAmount;
}
