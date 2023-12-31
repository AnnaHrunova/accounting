package com.mintos.accounting.service.account;

import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientEntity;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        val client = clientRepository.findById(command.getClientUUID())
                .orElseThrow(() -> new ResourceNotFoundException(Reason.CLIENT_NOT_FOUND, command.getClientUUID().toString()));
        val account = new AccountEntity();
        account.setClient(client);
        account.setCurrency(command.getCurrency());
        account.setBalance(toMoney(command.getInitialAmount()));
        return accountRepository.save(account).getId();
    }

    public AccountData getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(accountMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(Reason.ACCOUNT_NOT_FOUND, accountId.toString()));
    }

    public List<AccountData> getClientAccounts(UUID clientUUID) {
        return accountRepository.findAllByClientIdOrderByCreatedDateDesc(clientUUID)
                .stream()
                .map(accountMapper::map)
                .collect(Collectors.toList());
    }

}
