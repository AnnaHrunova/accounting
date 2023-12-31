package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountCommand {

    @NotNull
    private UUID clientUUID;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal initialAmount;
}
