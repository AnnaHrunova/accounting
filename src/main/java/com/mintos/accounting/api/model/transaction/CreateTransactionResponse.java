package com.mintos.accounting.api.model.transaction;


import com.mintos.accounting.common.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Create transaction response")
public class CreateTransactionResponse {

    @Schema(
            description = "Transaction unique identifier",
            example = "0016ec5b-8196-46f3-834c-8546ac30183a")
    private UUID transactionUUID;

    @Schema(
            description = "Transaction processing date and time",
            example = "2023-11-20T08:15:04.109Z")
    private OffsetDateTime dateTime;

    @Schema(
            description = "Transaction status",
            example = "SUCCESS")
    private TransactionStatus status;
}
