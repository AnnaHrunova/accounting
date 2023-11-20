package com.mintos.accounting.api.model.account;

import com.mintos.accounting.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Account creation request")
public class CreateAccountRequest {

    @Schema(
            description = "Account currency",
            example = "EUR")
    @NotNull
    private Currency currency;

    @Schema(
            description = "Account initial balance",
            example = "100.00")
    @NotNull
    @DecimalMin(value = "0.01", inclusive = false)
    private BigDecimal balance;
}
