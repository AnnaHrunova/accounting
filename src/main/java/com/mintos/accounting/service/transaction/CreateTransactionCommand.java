package com.mintos.accounting.service.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionCommand {

    @NotBlank
    private String requestId;

    @NotBlank
    private String fromAccountUUID;

    @NotBlank
    private String toAccountUUID;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionStatus status;

    private BigDecimal convertedAmount;
}
