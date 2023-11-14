package com.mintos.accounting.service;

import com.mintos.accounting.api.model.CreateAccountRequest;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateClientRequest;
import com.mintos.accounting.api.model.CreateClientResponse;
import com.mintos.accounting.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@AllArgsConstructor
@Validated
public class AccountingService {

    private final AccountService accountService;

    public CreateClientResponse createClient(@Valid CreateClientRequest request) {
        val command = CreateClientCommand.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        val clientUUID = accountService.createClient(command);
        return CreateClientResponse.builder()
                .clientUUID(clientUUID.toString()).build();
    }

    public CreateAccountResponse createAccount(String clientId, @Valid CreateAccountRequest request) {
        val command = CreateAccountCommand.builder()
                .clientUUID(clientId)
                .currency(request.getCurrency())
                .build();

        val accountUUID = accountService.createAccount(command);
        return CreateAccountResponse.builder()
                .accountUUID(accountUUID.toString()).build();
    }
}
