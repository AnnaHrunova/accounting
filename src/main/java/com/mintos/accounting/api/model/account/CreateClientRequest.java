package com.mintos.accounting.api.model.account;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client creation request")
public class CreateClientRequest {

    @Schema(
            description = "Client first name",
            example = "John")
    @NotBlank
    private String firstName;

    @Schema(
            description = "Client last name",
            example = "Doe")
    @NotBlank
    private String lastName;
}
