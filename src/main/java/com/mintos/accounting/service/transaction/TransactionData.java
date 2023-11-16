package com.mintos.accounting.service.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TransactionData {

    private UUID id;

    private String requestId;

    private TransactionStatus status;

    private Currency currency;

    private BigDecimal amount;

    private BigDecimal convertedAmount;

    private OffsetDateTime createdDate;
}
