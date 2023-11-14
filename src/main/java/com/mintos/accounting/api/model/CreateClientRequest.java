package com.mintos.accounting.api.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
