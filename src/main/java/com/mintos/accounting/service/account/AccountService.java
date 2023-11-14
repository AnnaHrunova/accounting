package com.mintos.accounting.service.account;

import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientEntity;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.service.CreateAccountCommand;
import com.mintos.accounting.service.CreateClientCommand;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Component
@Validated
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public UUID createClient(@Valid CreateClientCommand command) {
        val client = new ClientEntity();
        client.setFirstName(command.getFirstName());
        client.setLastName(command.getLastName());
        return clientRepository.save(client).getId();
    }

    public UUID createAccount(@Valid CreateAccountCommand command) {
        val client = clientRepository.findById(UUID.fromString(command.getClientUUID()))
                .orElseThrow();
        val account = new AccountEntity();
        account.setClient(client);
        account.setCurrency(command.getCurrency());
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account).getId();
    }

}