package com.mintos.accounting.service;

import com.mintos.accounting.api.model.*;
import com.mintos.accounting.service.account.AccountData;
import com.mintos.accounting.service.account.AccountService;
import com.mintos.accounting.service.account.CreateAccountCommand;
import com.mintos.accounting.service.account.CreateClientCommand;
import com.mintos.accounting.service.converter.CurrencyExchangeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mintos.accounting.service.account.AccountRules.validateAccount;

@Component
@AllArgsConstructor
@Validated
public class AccountingService {

    private final AccountService accountService;
    private final CurrencyExchangeService exchangeService;

    public CreateClientResponse createClient(@Valid CreateClientRequest request) {
        val command = CreateClientCommand.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        val clientUUID = accountService.createClient(command);
        return CreateClientResponse.builder()
                .clientUUID(clientUUID).build();
    }

    public CreateAccountResponse createAccount(UUID clientId, @Valid CreateAccountRequest request) {
        val command = CreateAccountCommand.builder()
                .clientUUID(clientId)
                .currency(request.getCurrency())
                .initialAmount(request.getBalance())
                .build();

        validateAccount(command, exchangeService.getSupportedCurrencies());
        val accountUUID = accountService.createAccount(command);
        return CreateAccountResponse.builder()
                .accountUUID(accountUUID).build();
    }

    public List<AccountDataResponse> getClientAccounts(UUID clientId) {
        return accountService.getClientAccounts(clientId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private AccountDataResponse map(AccountData accountData) {
        return AccountDataResponse.builder()
                .currency(accountData.getCurrency())
                .balance(accountData.getBalance())
                .accountId(accountData.getId())
                .build();
    }
}
