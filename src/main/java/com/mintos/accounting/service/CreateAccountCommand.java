package com.mintos.accounting.service;

import com.mintos.accounting.common.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountCommand {

    @NotBlank
    private String clientUUID;

    @NotNull
    private Currency currency;
}
