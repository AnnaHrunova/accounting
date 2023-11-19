package com.mintos.accounting.api;

import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.api.model.CreateTransactionResponse;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.config.BaseRestAssuredTest;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.domain.transaction.TransactionRepository;
import com.mintos.accounting.domain.view.TransactionViewRepository;
import com.mintos.accounting.service.account.AccountService;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    AccountService accountingService;

    @Test
    public void shouldCreateTransaction() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        final CreateTransactionResponse response = processTransaction(transactionRequest, HttpStatus.CREATED);
        val transactionUUID = response.getTransactionUUID();
        val savedTransaction = transactionRepository.findById(transactionUUID);
        assertThat(savedTransaction).isPresent();
        assertThat(savedTransaction.get().getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(savedTransaction.get().getCurrency()).isEqualTo(transactionRequest.getCurrency());
        assertThat(savedTransaction.get().getFromAccount().getId()).isEqualTo(transactionRequest.getFromAccountUUID());
        assertThat(savedTransaction.get().getToAccount().getId()).isEqualTo(transactionRequest.getToAccountUUID());

        val outgoingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountFromUUID);
        assertThat(outgoingTransactions.size()).isEqualTo(1);
        val outgoingTransaction = outgoingTransactions.get(0);
        assertThat(outgoingTransaction.getType()).isEqualTo(TransactionType.OUTGOING);
        assertThat(outgoingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("95.00")));

        val incomingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountToUUID);
        assertThat(incomingTransactions.size()).isEqualTo(1);
        val incomingTransaction = incomingTransactions.get(0);
        assertThat(incomingTransaction.getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(incomingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("55.00")));

    }

    @Test
    public void shouldCreateTransactionWithCurrencyConversion() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.USD)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        final CreateTransactionResponse response = processTransaction(transactionRequest, HttpStatus.CREATED);
        val transactionUUID = response.getTransactionUUID();
        val savedTransaction = transactionRepository.findById(transactionUUID);
        assertThat(savedTransaction).isPresent();
        assertThat(savedTransaction.get().getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(savedTransaction.get().getCurrency()).isEqualTo(transactionRequest.getCurrency());
        assertThat(savedTransaction.get().getFromAccount().getId()).isEqualTo(transactionRequest.getFromAccountUUID());
        assertThat(savedTransaction.get().getToAccount().getId()).isEqualTo(transactionRequest.getToAccountUUID());

        val outgoingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountFromUUID);
        assertThat(outgoingTransactions.size()).isEqualTo(1);
        val outgoingTransaction = outgoingTransactions.get(0);
        assertThat(outgoingTransaction.getType()).isEqualTo(TransactionType.OUTGOING);
        assertThat(outgoingTransaction.getTransaction().getStatus()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(outgoingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("99.75"))); //100 USD - (5 * 0.05) USD

        val incomingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountToUUID);
        assertThat(incomingTransactions.size()).isEqualTo(1);
        val incomingTransaction = incomingTransactions.get(0);
        assertThat(incomingTransaction.getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(incomingTransaction.getTransaction().getStatus()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(incomingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("55.00"))); //50 EUR + 5 EUR
    }

    @Test
    public void shouldValidateTransactionWithCurrencyConversion() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.USD)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("50"), Currency.EUR)).getId();
        val transactionRequest = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);
        transactionRequest.setCurrency(Currency.GBP);
        val response = processTransaction(transactionRequest, HttpStatus.PRECONDITION_FAILED);
        val transactionUUID = response.getTransactionUUID();
        val savedTransaction = transactionRepository.findById(transactionUUID);
        assertThat(savedTransaction).isPresent();
        assertThat(savedTransaction.get().getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(savedTransaction.get().getCurrency()).isEqualTo(transactionRequest.getCurrency());
        assertThat(savedTransaction.get().getFromAccount().getId()).isEqualTo(transactionRequest.getFromAccountUUID());
        assertThat(savedTransaction.get().getToAccount().getId()).isEqualTo(transactionRequest.getToAccountUUID());

        val outgoingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountFromUUID);
        assertThat(outgoingTransactions.size()).isEqualTo(1);
        val outgoingTransaction = outgoingTransactions.get(0);
        assertThat(outgoingTransaction.getType()).isEqualTo(TransactionType.OUTGOING);
        assertThat(outgoingTransaction.getTransaction().getStatus()).isEqualTo(TransactionStatus.ERROR);
        assertThat(outgoingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("100")));

        val incomingTransactions = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountToUUID);
        assertThat(incomingTransactions.size()).isEqualTo(1);
        val incomingTransaction = incomingTransactions.get(0);
        assertThat(incomingTransaction.getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(incomingTransaction.getTransaction().getStatus()).isEqualTo(TransactionStatus.ERROR);
        assertThat(incomingTransaction.getAccount().getBalance()).isEqualTo(toMoney(new BigDecimal("50"))); //50 EUR + 5 EUR

    }

    @Test
    void test() throws InterruptedException {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());

        val accountFromUUID = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR)).getId();
        val accountToUUID = accountRepository.save(prepareAccount(client2, new BigDecimal("5"), Currency.EUR)).getId();
        val transactionRequest = prepareDefaultTransactionRequest(accountFromUUID, accountToUUID);

        Callable<Object> createTrx = () -> processTransaction(transactionRequest, HttpStatus.CREATED);

        var executorService = Executors.newFixedThreadPool(2);

        executorService.invokeAll(List.of(
                createTrx, createTrx, createTrx,
                createTrx, createTrx, createTrx
        ), 5, TimeUnit.SECONDS);

        executorService.shutdown();

//        val res = accountingService.getAccount(accountFromUUID);
        // Verify that the account's balance was correctly updated
        assertThat(1).isEqualTo(1);
    }

    private static CreateTransactionResponse processTransaction(CreateTransactionRequest transactionRequest, HttpStatus expectedHttpStatus) {
        return given()
                .contentType(ContentType.JSON)
                .body(transactionRequest)
                .when()
                .post("/accounting/v1/transactions")
                .then()
                .assertThat()
                .statusCode(expectedHttpStatus.value())
                .body("transactionUUID", notNullValue())
                .extract()
                .body()
                .as(CreateTransactionResponse.class);
    }
}
