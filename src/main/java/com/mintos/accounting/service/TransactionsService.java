package com.mintos.accounting.service;

import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.api.model.CreateTransactionResponse;
import com.mintos.accounting.api.model.FilteredPageRequest;
import com.mintos.accounting.api.model.TransactionViewResponse;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.exceptions.TransactionValidationException;
import com.mintos.accounting.service.account.AccountData;
import com.mintos.accounting.service.account.AccountService;
import com.mintos.accounting.service.converter.CurrencyExchangeService;
import com.mintos.accounting.service.transaction.CreateTransactionCommand;
import com.mintos.accounting.service.transaction.TransactionData;
import com.mintos.accounting.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static com.mintos.accounting.service.transaction.TransactionRules.validateTransaction;

@Component
@AllArgsConstructor
@Validated
public class TransactionsService {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CurrencyExchangeService exchangeService;
    private final CommandMapper commandMapper;

    public CreateTransactionResponse createTransaction(@Valid CreateTransactionRequest request) {
        val fromAccount = accountService.getAccount(request.getFromAccountUUID());
        val toAccount = accountService.getAccount(request.getToAccountUUID());
        val command = commandMapper.map(request);
        val targetCurrency = request.getCurrency();
        val originCurrency = fromAccount.getCurrency();
        if (originCurrency != targetCurrency) {
            command.setConvertedAmount(exchangeService.convert(command.getAmount(), targetCurrency, originCurrency));
        }
        val savedTransaction = performTransaction(command, fromAccount, toAccount);
        return CreateTransactionResponse.builder()
                .transactionUUID(savedTransaction.getId())
                .dateTime(savedTransaction.getCreatedDate())
                .status(savedTransaction.getStatus())
                .build();

    }

    public Page<TransactionViewResponse> getHistory(String accountUUID, FilteredPageRequest pageRequest) {
        val transactionsPage = transactionService.getHistory(accountUUID, pageRequest);
        return new PageImpl<>(
                transactionsPage.toList(),
                transactionsPage.getPageable(),
                transactionsPage.getTotalElements());
    }

    private TransactionData performTransaction(CreateTransactionCommand command, AccountData fromAccount, AccountData toAccount) {
        TransactionData savedTransaction;
        try {
            validateTransaction(fromAccount, toAccount, command, exchangeService.getSupportedCurrencies());
            command.setStatus(TransactionStatus.SUCCESS);
            savedTransaction = transactionService.performTransaction(command);
        } catch (TransactionValidationException ex) {
            command.setStatus(TransactionStatus.ERROR);
            savedTransaction = transactionService.handleFailedTransaction(command);
        }
        return savedTransaction;
    }
}
