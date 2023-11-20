package com.mintos.accounting.api.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Account creation response")
public class CreateAccountResponse {

    @Schema(
            description = "Account unique identifier",
            example = "42f23b91-7294-4703-8172-b56ac782cc83")
    private UUID accountUUID;
}
