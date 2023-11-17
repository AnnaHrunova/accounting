package com.mintos.accounting.service.account;

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
public class CreateAccountCommand {

    @NotBlank
    private String clientUUID;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal initialAmount;
}
