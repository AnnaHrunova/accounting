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
        description = "Client creation response")
public class CreateClientResponse {

    @Schema(
            description = "Client unique identifier",
            example = "0016ec5b-8196-46f3-834c-8546ac30183a")
    private UUID clientUUID;
}
