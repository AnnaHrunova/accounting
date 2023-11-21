package com.mintos.accounting.api.model.transaction;


import com.mintos.accounting.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Request for making a money transaction")
public class CreateTransactionRequest {

    @Schema(
            description = "Transaction external unique identifier from transaction initiator",
            example = "Bank-Transaction-9988776655")
    @NotBlank
    private String requestId;

    @Schema(
            description = "Initiator account unique identifier",
            example = "42f23b91-7294-4703-8172-b56ac782cc83")
    @NotNull
    private UUID fromAccountUUID;

    @Schema(
            description = "Receiver account unique identifier",
            example = "40031e35-78e0-4105-a2a1-603039ca0578")
    @NotNull
    private UUID toAccountUUID;

    @Schema(
            description = "Amount to be sent",
            example = "100.00")
    @NotNull
    @DecimalMin(value = "0.01", inclusive = false)
    private BigDecimal amount;

    @Schema(
            description = "Currency of amount to be sent",
            example = "EUR")
    @NotNull
    private Currency currency;
}
