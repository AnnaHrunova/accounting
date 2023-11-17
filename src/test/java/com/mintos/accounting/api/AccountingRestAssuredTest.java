package com.mintos.accounting.api;

import com.mintos.accounting.api.error.RestValidationResponse;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateClientResponse;
import com.mintos.accounting.config.BaseRestAssuredTest;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientRepository;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.mintos.accounting.TestFixtures.*;
import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class AccountingRestAssuredTest extends BaseRestAssuredTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldCreateClient() {
        val newClientRequest = prepareClientRequest();
        val response =
                given()
                        .contentType(ContentType.JSON)
                        .body(newClientRequest)
                        .when()
                        .post("/accounting/v1/clients")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("clientUUID", notNullValue())
                        .extract()
                        .body()
                        .as(CreateClientResponse.class);
        val newClientUUID = response.getClientUUID();
        val savedClient = clientRepository.findById(newClientUUID);
        assertThat(savedClient).isPresent();
        assertThat(savedClient.get().getFirstName()).isEqualTo(newClientRequest.getFirstName());
        assertThat(savedClient.get().getLastName()).isEqualTo(newClientRequest.getLastName());
    }

    @Test
    public void shouldCreateAccount() {
        val newClient = prepareClient();
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

    @Test
    public void shouldValidateAccountCreation() {
        val newClient = prepareClient();
        val newClientUUID = clientRepository.save(newClient).getId();

        val newAccountRequest = prepareAccountRequest(new BigDecimal("-100"));
        val response =
                given()
                        .contentType(ContentType.JSON)
                        .body(newAccountRequest)
                        .when()
                        .post("/accounting/v1/clients/{1}/accounts", newClientUUID)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .extract()
                        .body()
                        .as(RestValidationResponse.class);
        assertThat(response.getFieldValidationErrors().size()).isEqualTo(1);
        assertThat(response.getFieldValidationErrors().get(0).field()).isEqualTo("balance");
        assertThat(response.getFieldValidationErrors().get(0).message()).isEqualTo("must be greater than 0.01");
    }
}
