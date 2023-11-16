package com.mintos.accounting.api;

import com.mintos.accounting.config.BaseRestAssuredTest;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateTransactionResponse;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientEntity;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.domain.transaction.TransactionRepository;
import com.mintos.accounting.domain.view.TransactionViewRepository;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static com.mintos.accounting.TestFixtures.*;
import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class TransactionsRestAssuredTest extends BaseRestAssuredTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionViewRepository transactionViewRepository;

    @Test
    public void shouldCreateTransaction() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest = prepareTransactionRequest(accountFromUUID.toString(), accountToUUID.toString(), new BigDecimal("30"), Currency.EUR);
        val response =
                given()
                        .contentType(ContentType.JSON)
                        .body(transactionRequest)
                        .when()
                        .post("/accounting/v1/transactions")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("transactionUUID", notNullValue())
                        .extract()
                        .body()
                        .as(CreateTransactionResponse.class);
        val transactionUUID = UUID.fromString(response.getTransactionUUID());
        val savedTransaction = transactionRepository.findById(transactionUUID);
        assertThat(savedTransaction).isPresent();
        assertThat(savedTransaction.get().getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(savedTransaction.get().getCurrency()).isEqualTo(transactionRequest.getCurrency());
        assertThat(savedTransaction.get().getFromAccount().getId().toString()).isEqualTo(transactionRequest.getFromAccountUUID());
        assertThat(savedTransaction.get().getToAccount().getId().toString()).isEqualTo(transactionRequest.getToAccountUUID());

        val outgoingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountFromUUID);
        assertThat(outgoingTransactions.size()).isEqualTo(1);
        val outgoingTransaction = outgoingTransactions.get(0);
        assertThat(outgoingTransaction.getType()).isEqualTo(TransactionType.OUTGOING);
        assertThat(outgoingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("70")));

        val incomingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountToUUID);
        assertThat(incomingTransactions.size()).isEqualTo(1);
        val incomingTransaction = incomingTransactions.get(0);
        assertThat(incomingTransaction.getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(incomingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("80")));

    }

    @Test
    public void shouldCreateAccount() {
        val newClient = new ClientEntity();
        newClient.setFirstName("Jane");
        newClient.setLastName("Smith");
        val newClientUUID = clientRepository.save(newClient).getId();

        val newAccountRequest = prepareAccountRequest();
        val response =
                given()
                        .contentType(ContentType.JSON)
                        .body(newAccountRequest)
                        .when()
                        .post("/accounting/v1/clients/{1}/accounts", newClientUUID)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("accountUUID", notNullValue())
                        .extract()
                        .body()
                        .as(CreateAccountResponse.class);
        val newAccountUUID = UUID.fromString(response.getAccountUUID());
        val savedAccount = accountRepository.findById(newAccountUUID);
        assertThat(savedAccount).isPresent();
        assertThat(savedAccount.get().getBalance()).isEqualTo(toMoney(BigDecimal.ZERO));
        assertThat(savedAccount.get().getCurrency()).isEqualTo(newAccountRequest.getCurrency());
    }

}
