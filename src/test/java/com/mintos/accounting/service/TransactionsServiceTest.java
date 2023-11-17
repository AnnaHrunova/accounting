package com.mintos.accounting.service;

import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.config.BaseIntegrationTest;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.client.ClientRepository;
import com.mintos.accounting.domain.transaction.TransactionRepository;
import com.mintos.accounting.domain.view.TransactionViewRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static com.mintos.accounting.TestFixtures.prepareAccount;
import static com.mintos.accounting.TestFixtures.prepareClient;
import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TransactionsServiceTest extends BaseIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionViewRepository transactionViewRepository;

    @Autowired
    private TransactionsService target;

    @Test
    void shouldCreateTransaction_Success() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());
        val accountFrom = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR));
        val accountTo = accountRepository.save(prepareAccount(client2, new BigDecimal("200"), Currency.EUR));

        val requestId = UUID.randomUUID().toString();
        val transactionRequest = CreateTransactionRequest.builder()
                .requestId(requestId)
                .amount(new BigDecimal("10"))
                .fromAccountUUID(accountFrom.getId().toString())
                .toAccountUUID(accountTo.getId().toString())
                .currency(Currency.EUR)
                .build();
        val response = target.createTransaction(transactionRequest);
        assertThat(response).isNotNull();
        assertThat(response.getTransactionUUID()).isNotNull();

        val transaction = transactionRepository.findById(response.getTransactionUUID()).orElseThrow();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void shouldCreateTransactionWithConversion_Success() {
        val client1 = clientRepository.save(prepareClient());
        val client2 = clientRepository.save(prepareClient());
        val accountFrom = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR));
        val accountTo = accountRepository.save(prepareAccount(client2, new BigDecimal("200"), Currency.GBP));

        val requestId = UUID.randomUUID().toString();
        val transactionRequest = CreateTransactionRequest.builder()
                .requestId(requestId)
                .amount(new BigDecimal("10"))
                .fromAccountUUID(accountFrom.getId().toString())
                .toAccountUUID(accountTo.getId().toString())
                .currency(Currency.GBP)
                .build();
        val response = target.createTransaction(transactionRequest);
        assertThat(response).isNotNull();
        assertThat(response.getTransactionUUID()).isNotNull();

        val transaction = transactionRepository.findById(response.getTransactionUUID()).orElseThrow();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
        assertThat(transaction.getAmount()).isEqualTo(toMoney(transactionRequest.getAmount()));
        assertThat(transaction.getCurrency()).isEqualTo(transactionRequest.getCurrency());

        val historyOutgoing = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountFrom.getId()).get(0);
        val historyIncoming = transactionViewRepository.findAllByAccount_IdOrderByCreatedDateDesc(accountTo.getId()).get(0);

        assertThat(historyOutgoing).isNotNull();
        assertThat(historyOutgoing.getAccount().getId()).isEqualTo(accountFrom.getId());
        assertThat(historyOutgoing.getType()).isEqualTo(TransactionType.OUTGOING);
        assertThat(historyOutgoing.getConvertedAmount()).isNotNull();
        assertThat(historyOutgoing.getConvertedFrom()).isEqualTo(transactionRequest.getCurrency());

        assertThat(historyIncoming).isNotNull();
        assertThat(historyIncoming.getAccount().getId()).isEqualTo(accountTo.getId());
        assertThat(historyIncoming.getType()).isEqualTo(TransactionType.INCOMING);
        assertThat(historyIncoming.getConvertedAmount()).isNull();
        assertThat(historyIncoming.getConvertedFrom()).isNull();
    }

    @Test
    void shouldValidateTransaction_SameAccount() {
        val client1 = clientRepository.save(prepareClient());
        val account = accountRepository.save(prepareAccount(client1, new BigDecimal("100"), Currency.EUR));

        val requestId = UUID.randomUUID().toString();
        val transactionRequest = CreateTransactionRequest.builder()
                .requestId(requestId)
                .amount(new BigDecimal("10"))
                .fromAccountUUID(account.getId().toString())
                .toAccountUUID(account.getId().toString())
                .currency(Currency.EUR)
                .build();
        val response = target.createTransaction(transactionRequest);
        assertThat(response).isNotNull();
        assertThat(response.getTransactionUUID()).isNotNull();
        val transaction = transactionRepository.findById(response.getTransactionUUID()).orElseThrow();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.ERROR);
    }
}