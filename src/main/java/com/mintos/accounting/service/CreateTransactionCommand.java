package com.mintos.accounting.service;

import com.mintos.accounting.common.Currency;
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
    private String fromAccountUUID;

    @NotBlank
    private String toAccountUUID;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    private BigDecimal convertedAmount;
}
