package com.mintos.accounting.api.model;


import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.common.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionViewResponse {

    private String requestId;

    private UUID transactionUUID;

    private TransactionType type;

    private Currency currency;

    private BigDecimal amount;

    private TransactionStatus status;

    private OffsetDateTime dateTime;
}
