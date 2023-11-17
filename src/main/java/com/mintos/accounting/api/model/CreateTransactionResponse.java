package com.mintos.accounting.api.model;


import com.mintos.accounting.common.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionResponse {

    private UUID transactionUUID;

    private OffsetDateTime dateTime;

    private TransactionStatus status;
}
