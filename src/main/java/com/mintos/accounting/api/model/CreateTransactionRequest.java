package com.mintos.accounting.api.model;


import com.mintos.accounting.common.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    @NotBlank
    private String requestId;

    @NotBlank
    private String fromAccountUUID;

    @NotBlank
    private String toAccountUUID;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Currency currency;
}
