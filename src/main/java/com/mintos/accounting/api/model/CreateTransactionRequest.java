package com.mintos.accounting.api.model;


import com.mintos.accounting.common.Currency;
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
public class CreateTransactionRequest {

    @NotBlank
    private String requestId;

    @NotNull
    private UUID fromAccountUUID;

    @NotNull
    private UUID toAccountUUID;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Currency currency;
}
