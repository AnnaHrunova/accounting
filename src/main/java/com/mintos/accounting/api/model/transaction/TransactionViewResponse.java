package com.mintos.accounting.api.model.transaction;


import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.common.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Transaction view from account perspective")
public class TransactionViewResponse {

    @Schema(
            description = "Transaction external unique identifier from transaction initiator",
            example = "Bank-Transaction-9988776655")
    private String requestId;

    @Schema(
            description = "Transaction unique identifier",
            example = "0016ec5b-8196-46f3-834c-8546ac30183a")
    private UUID transactionUUID;

    @Schema(
            description = "Transaction type from account perspective",
            example = "INCOMING")
    private TransactionType type;

    @Schema(
            description = "Transaction currency",
            example = "EUR")
    private Currency currency;

    @Schema(
            description = "Transaction amount",
            example = "50.00")
    private BigDecimal amount;

    @Schema(
            description = "Transaction status",
            example = "SUCCESS")
    private TransactionStatus status;

    private OffsetDateTime dateTime;
}
