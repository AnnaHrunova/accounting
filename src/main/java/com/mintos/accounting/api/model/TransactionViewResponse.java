package com.mintos.accounting.api.model;


import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionViewResponse {

    private TransactionType type;

    private Currency currency;

    private BigDecimal amount;

    private OffsetDateTime dateTime;
}
