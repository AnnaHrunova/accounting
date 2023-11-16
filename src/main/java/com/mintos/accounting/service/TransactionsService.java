package com.mintos.accounting.service;

import com.mintos.accounting.api.FilteredPageRequest;
import com.mintos.accounting.api.model.*;
import com.mintos.accounting.service.account.AccountService;
import com.mintos.accounting.service.account.TransactionMapper;
import com.mintos.accounting.service.account.TransactionRules;
import com.mintos.accounting.service.account.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static com.mintos.accounting.service.account.TransactionRules.*;

@Component
@AllArgsConstructor
@Validated
public class TransactionsService {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final CurrencyConverter currencyConverter;

    public CreateTransactionResponse createTransaction(@Valid CreateTransactionRequest request) {
        val fromAccount = accountService.getAccount(request.getFromAccountUUID());
        val toAccount = accountService.getAccount(request.getToAccountUUID());
        val transaction = transactionMapper.map(request);
        if (fromAccount.getCurrency() != transaction.getCurrency()) {
            transaction.setConvertedAmount(currencyConverter.convert(transaction.getAmount(), transaction.getCurrency(), fromAccount.getCurrency()));
        }

        validateTransaction(fromAccount, toAccount, transaction);

        val command = transactionMapper.mapToCommand(request);
        val transactionUUID = transactionService.createTransaction(command);
        return CreateTransactionResponse.builder()
                .transactionUUID(transactionUUID.toString())
                .build();
    }

    public Page<TransactionViewResponse> getHistory(String accountUUID, FilteredPageRequest pageRequest) {
        val transactionsPage = transactionService.getHistory(accountUUID, pageRequest);
        return new PageImpl<>(
                transactionsPage.map(transactionMapper::map).toList(),
                transactionsPage.getPageable(),
                transactionsPage.getTotalElements());
    }
}
