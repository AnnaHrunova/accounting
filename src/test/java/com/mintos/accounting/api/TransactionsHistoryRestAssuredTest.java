package com.mintos.accounting.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.api.model.CreateTransactionResponse;
import com.mintos.accounting.api.model.TransactionViewResponse;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.config.BaseRestAssuredTest;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientEntity;
import com.mintos.accounting.domain.client.ClientRepository;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnAccountTransactionsHistory() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest1 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        val transactionRequest2 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        val transactionRequest3 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);

        val transactionRequest4 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        val transactionRequest5 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        val transactionRequest6 = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);

        val savedTransaction1 = makeTransaction(transactionRequest1);
        val savedTransaction2 = makeTransaction(transactionRequest2);
        val savedTransaction3 = makeTransaction(transactionRequest3);

        val savedTransaction4 = makeTransaction(transactionRequest4);
        val savedTransaction5 = makeTransaction(transactionRequest5);
        val savedTransaction6 = makeTransaction(transactionRequest6);

        val response1 = getTransactionHistoryOK(accountToUUID, 0, 1);
        assertThat(response1).isNotNull();
        val transactionsHistory1 = getTransactionsHistory(response1.get("content"));
        assertThat(transactionsHistory1.size()).isEqualTo(1);
        assertThat(transactionsHistory1.get(0).getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(transactionsHistory1.get(0).getStatus()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(transactionsHistory1.get(0).getTransactionUUID()).isEqualTo(savedTransaction6.getTransactionUUID());

        val response2 = getTransactionHistoryOK(accountToUUID, 1, 2);
        assertThat(response2).isNotNull();
        val transactionsHistory2 = getTransactionsHistory(response2.get("content"));
        assertThat(transactionsHistory2.size()).isEqualTo(2);
        assertThat(transactionsHistory2.get(0).getTransactionUUID()).isEqualTo(savedTransaction4.getTransactionUUID());
        assertThat(transactionsHistory2.get(1).getTransactionUUID()).isEqualTo(savedTransaction3.getTransactionUUID());

        val response3 = getTransactionHistoryOK(accountToUUID, 10, 2);
        assertThat(response3).isNotNull();
        val transactionsHistory3 = getTransactionsHistory(response3.get("content"));
        assertThat(transactionsHistory3.size()).isEqualTo(0);

        getTransactionHistoryNOK(accountToUUID, -10, -2);
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
        val newAccountUUID = response.getAccountUUID();
        val savedAccount = accountRepository.findById(newAccountUUID);
        assertThat(savedAccount).isPresent();
        assertThat(savedAccount.get().getBalance()).isEqualTo(toMoney(newAccountRequest.getBalance()));
        assertThat(savedAccount.get().getCurrency()).isEqualTo(newAccountRequest.getCurrency());
    }

    private static CreateTransactionResponse makeTransaction(CreateTransactionRequest transactionRequest) {
        return given()
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

    private List<TransactionViewResponse> getTransactionsHistory(Object transactionsHistory) {
        return objectMapper.convertValue(transactionsHistory, new TypeReference<>() {
        });
    }

    private static Response getTransactionHistory(UUID accountToUUID, int offset, int limit) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/accounting/v1/accounts/{1}/transactions?offset={2}&limit={3}", accountToUUID, offset, limit);
    }

    private static JsonPath getTransactionHistoryOK(UUID accountToUUID, int offset, int limit) {
        return getTransactionHistory(accountToUUID, offset, limit)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath();
    }

    private static JsonPath getTransactionHistoryNOK(UUID accountToUUID, int offset, int limit) {
        return getTransactionHistory(accountToUUID, offset, limit).then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .jsonPath();
    }

}
