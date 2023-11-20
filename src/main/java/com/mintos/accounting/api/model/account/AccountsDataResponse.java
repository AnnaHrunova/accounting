package com.mintos.accounting.api.model.account;

import com.mintos.accounting.api.model.account.AccountDataResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "List of client accounts")
public class AccountsDataResponse {

    @Schema(
            description = "Client accounts")
    private List<AccountDataResponse> accounts;
}
