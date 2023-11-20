package com.mintos.accounting.service;

import com.mintos.accounting.api.model.account.AccountDataResponse;
import com.mintos.accounting.api.model.account.CreateAccountRequest;
import com.mintos.accounting.api.model.account.CreateClientRequest;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.config.BaseMockIntegrationTest;
import com.mintos.accounting.exceptions.AccountValidationException;
import com.mintos.accounting.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.mintos.accounting.TestFixtures.prepareAccountRequest;
import static com.mintos.accounting.TestFixtures.prepareClientRequest;
import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountingServiceTest extends BaseMockIntegrationTest {

    @Autowired
    private AccountingService target;

    @Test
    void shouldCreateClient() {
        val request = new CreateClientRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        val result = target.createClient(request);
        assertThat(result.getClientUUID()).isNotNull();
    }


    @Test
    void shouldValidateCreateClientRequest() {
        val request = new CreateClientRequest();
        request.setFirstName("John");
        assertThrows(ConstraintViolationException.class, () -> target.createClient(request));
    }

    @Test
    void shouldCreateAccount() {
        val clientRequest = prepareClientRequest();
        val clientUUID = target.createClient(clientRequest).getClientUUID();
        List<AccountDataResponse> clientAccounts = target.getClientAccounts(clientUUID);
        assertThat(clientAccounts.size()).isEqualTo(0);

        val accountRequest = prepareAccountRequest(new BigDecimal("100"));
        val response = target.createAccount(clientUUID, accountRequest);
        assertThat(response).isNotNull();
        assertThat(response.getAccountUUID()).isNotNull();
        clientAccounts = target.getClientAccounts(clientUUID);
        assertThat(clientAccounts.size()).isEqualTo(1);
        assertAccount(clientAccounts.get(0), accountRequest);
    }


    @Test
    void shouldNotCreateAccount_NegativeBalance() {
        val clientRequest = prepareClientRequest();
        val clientUUID = target.createClient(clientRequest).getClientUUID();

        val accountRequest = prepareAccountRequest(new BigDecimal("-10"));
        assertThrows(ConstraintViolationException.class, () -> target.createAccount(clientUUID, accountRequest));
        List<AccountDataResponse> clientAccounts = target.getClientAccounts(clientUUID);
        assertThat(clientAccounts.size()).isEqualTo(0);
    }

    @Test
    void shouldNotCreateAccount_NoClient() {
        val clientRequest = prepareClientRequest();
        val clientUUID = target.createClient(clientRequest).getClientUUID();

        val accountRequest = prepareAccountRequest(new BigDecimal("10"));
        assertThrows(ResourceNotFoundException.class, () -> target.createAccount(UUID.randomUUID(), accountRequest));
        List<AccountDataResponse> clientAccounts = target.getClientAccounts(clientUUID);
        assertThat(clientAccounts.size()).isEqualTo(0);
    }

    @Test
    void shouldNotCreateAccount_UnsupportedCurrency() {
        val clientRequest = prepareClientRequest();
        val clientUUID = target.createClient(clientRequest).getClientUUID();

        val accountRequest = prepareAccountRequest(new BigDecimal("10"));
        accountRequest.setCurrency(Currency.QQQ);
        assertThrows(AccountValidationException.class, () -> target.createAccount(UUID.randomUUID(), accountRequest));
        List<AccountDataResponse> clientAccounts = target.getClientAccounts(clientUUID);
        assertThat(clientAccounts.size()).isEqualTo(0);
    }

    private void assertAccount(AccountDataResponse accountData, CreateAccountRequest expectedData) {
        assertThat(accountData.getBalance()).isEqualTo(toMoney(expectedData.getBalance()));
        assertThat(accountData.getCurrency()).isEqualTo(expectedData.getCurrency());
    }


}