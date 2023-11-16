package com.mintos.accounting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.api.model.CreateTransactionResponse;
import com.mintos.accounting.api.model.TransactionViewResponse;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.config.BaseRestAssuredTest;
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
import java.util.List;
import java.util.UUID;

import static com.mintos.accounting.TestFixtures.*;
import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class TransactionsHistoryRestAssuredTest extends BaseRestAssuredTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionViewRepository transactionViewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnAccountTransactionsHistory() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest1 = prepareTransactionRequest(accountFromUUID.toString(), accountToUUID.toString(), new BigDecimal("30"), Currency.EUR);
        val transactionRequest2 = prepareTransactionRequest(accountFromUUID.toString(), accountToUUID.toString(), new BigDecimal("30"), Currency.EUR);
        val transactionRequest3 = prepareTransactionRequest(accountFromUUID.toString(), accountToUUID.toString(), new BigDecimal("30"), Currency.EUR);
        makeTransaction(transactionRequest1);
        makeTransaction(transactionRequest2);
        makeTransaction(transactionRequest3);

        val response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/accounting/v1/accounts/{1}/transactions?offset=0&limit=1", accountToUUID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .get("content");
        assertThat(response).isNotNull();
        val transactions = objectMapper.convertValue(response, List.class);
        val transaction1 = objectMapper.convertValue(transactions.get(0), TransactionViewResponse.class);
        assertThat(transaction1.getType()).isEqualTo(TransactionType.INCOMING);
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

    private static void makeTransaction(CreateTransactionRequest transactionRequest) {
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
    }

}
