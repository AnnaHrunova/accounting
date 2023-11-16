package com.mintos.accounting.api.model;

import com.mintos.accounting.common.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @NotNull
    private Currency currency;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = false)
    private BigDecimal balance;
}
