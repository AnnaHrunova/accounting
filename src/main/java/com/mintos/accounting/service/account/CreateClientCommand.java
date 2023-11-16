package com.mintos.accounting.service.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientCommand {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
