package com.mintos.accounting;

import com.mintos.accounting.api.model.CreateAccountRequest;
import com.mintos.accounting.api.model.CreateClientRequest;
import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.client.ClientEntity;
import lombok.val;

import java.math.BigDecimal;
import java.util.UUID;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

public class TestFixtures {

    public static CreateClientRequest prepareClientRequest() {
        val request = new CreateClientRequest();
        request.setFirstName("David");
        request.setLastName("Black");
        return request;
    }

    public static CreateAccountRequest prepareAccountRequest() {
        val request = new CreateAccountRequest();
        request.setCurrency(Currency.EUR);
        return request;
    }

    public static CreateTransactionRequest prepareTransactionRequest(String accountFrom, String accountTo, BigDecimal amount, Currency currency) {
        val request = new CreateTransactionRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setAmount(toMoney(amount));
        request.setCurrency(currency);
        request.setFromAccountUUID(accountFrom);
        request.setToAccountUUID(accountTo);
        return request;
    }

    public static ClientEntity prepareClient() {
        val newClient = new ClientEntity();
        newClient.setFirstName("Jane");
        newClient.setLastName("Smith");
        return newClient;
    }

    public static AccountEntity prepareAccount(ClientEntity client, BigDecimal balance, Currency currency) {
        val newAccount = new AccountEntity();
        newAccount.setBalance(toMoney(balance));
        newAccount.setCurrency(currency);
        newAccount.setClient(client);
        return newAccount;
    }
}
