package com.mintos.accounting.api.model.account;

import com.mintos.accounting.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Account summary")
public class AccountDataResponse {

    @Schema(
            description = "Account unique identifier",
            example = "42f23b91-7294-4703-8172-b56ac782cc83")
    private UUID accountId;

    @Schema(
            description = "Account balance",
            example = "100.00")
    private BigDecimal balance;

    @Schema(
            description = "Account currency",
            example = "EUR")
    private Currency currency;
}
