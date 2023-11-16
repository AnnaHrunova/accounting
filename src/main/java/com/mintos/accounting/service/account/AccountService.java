package com.mintos.accounting.service.account;

import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientEntity;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.ResourceNotFoundException;
import com.mintos.accounting.service.CreateAccountCommand;
import com.mintos.accounting.service.CreateClientCommand;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

@AllArgsConstructor
@Component
@Validated
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    public UUID createClient(@Valid CreateClientCommand command) {
        val client = new ClientEntity();
        client.setFirstName(command.getFirstName());
        client.setLastName(command.getLastName());
        return clientRepository.save(client).getId();
    }

    public UUID createAccount(@Valid CreateAccountCommand command) {
        val client = clientRepository.findById(UUID.fromString(command.getClientUUID()))
                .orElseThrow(() -> new ResourceNotFoundException(Reason.CLIENT_NOT_FOUND, command.getClientUUID()));
        val account = new AccountEntity();
        account.setClient(client);
        account.setCurrency(command.getCurrency());
        account.setBalance(toMoney(BigDecimal.ZERO));
        return accountRepository.save(account).getId();
    }

    public AccountData getAccount(String accountUUID) {
       return accountRepository.findById(UUID.fromString(accountUUID))
                .map(accountMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(Reason.ACCOUNT_NOT_FOUND, accountUUID));
    }

}
