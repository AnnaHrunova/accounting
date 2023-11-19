package com.mintos.accounting.service.transaction;

import com.mintos.accounting.api.model.FilteredPageRequest;
import com.mintos.accounting.api.model.TransactionViewResponse;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.account.AccountRepository;
import com.mintos.accounting.domain.transaction.TransactionEntity;
import com.mintos.accounting.domain.transaction.TransactionRepository;
import com.mintos.accounting.domain.view.TransactionViewEntity;
import com.mintos.accounting.domain.view.TransactionViewRepository;
import com.mintos.accounting.domain.view.TransactionViewSpecification;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Component
@Validated
@Slf4j
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionViewRepository transactionViewRepository;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionData performTransaction(@Valid CreateTransactionCommand command) {
        val accountFrom = getAccount(command.getFromAccountUUID());
        val accountTo = getAccount(command.getToAccountUUID());

        val accountFromNewBalance = calculateOutgoingBalance(command, accountFrom.getBalance());
        updateAccount(accountFrom, accountFromNewBalance);

        val accountToNewBalance = accountTo.getBalance().add(command.getAmount());
        updateAccount(accountTo, accountToNewBalance);

        return saveTransaction(command, accountFrom, accountTo);
    }

    public TransactionData handleFailedTransaction(@Valid CreateTransactionCommand command) {
        val accountFrom = getAccount(command.getFromAccountUUID());
        val accountTo = getAccount(command.getToAccountUUID());

        return saveTransaction(command, accountFrom, accountTo);
    }

    public Page<TransactionViewResponse> getHistory(String accountUUID, FilteredPageRequest pageRequest) {
        TransactionViewSpecification spec =
                new TransactionViewSpecification(accountUUID);

        return transactionViewRepository.findAll(spec, pageRequest.getPageable())
                .map(mapper::map);
    }

    private AccountEntity getAccount(String accountUUID) {
        return accountRepository.findById(UUID.fromString(accountUUID))
                .orElseThrow(() -> new ResourceNotFoundException(Reason.ACCOUNT_NOT_FOUND, accountUUID));
    }

    private void updateAccount(AccountEntity accountFrom, BigDecimal accountFromNewBalance) {
        accountFrom.setBalance(accountFromNewBalance);
        accountRepository.save(accountFrom);
    }

    private static BigDecimal calculateOutgoingBalance(CreateTransactionCommand command, BigDecimal balance) {
        var subAmount = command.getAmount();
        if (command.getConvertedAmount() != null) {
            subAmount = command.getConvertedAmount();
        }
        return balance.subtract(subAmount);
    }

    private TransactionData saveTransaction(CreateTransactionCommand command, AccountEntity accountFrom, AccountEntity accountTo) {
        val transaction = new TransactionEntity();
        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);
        transaction.setAmount(command.getAmount());
        transaction.setCurrency(command.getCurrency());
        transaction.setStatus(command.getStatus());
        transaction.setRequestId(command.getRequestId());

        val savedTransaction = transactionRepository.save(transaction);
        saveOutgoingView(savedTransaction, accountFrom, command);
        saveIncomingView(savedTransaction, accountTo);

        return mapper.map(savedTransaction);
    }

    private void saveIncomingView(TransactionEntity savedTransaction, AccountEntity account) {
        val transactionViewFrom = new TransactionViewEntity();
        transactionViewFrom.setTransaction(savedTransaction);
        transactionViewFrom.setAccount(account);
        transactionViewFrom.setType(TransactionType.INCOMING);
        transactionViewRepository.save(transactionViewFrom);
    }

    private void saveOutgoingView(TransactionEntity savedTransaction,
                                  AccountEntity account,
                                  CreateTransactionCommand command) {
        val transactionViewFrom = new TransactionViewEntity();
        transactionViewFrom.setTransaction(savedTransaction);
        transactionViewFrom.setAccount(account);
        transactionViewFrom.setType(TransactionType.OUTGOING);
        if (command.getConvertedAmount() != null && command.getCurrency() != null) {
            transactionViewFrom.setConvertedFrom(command.getCurrency());
            transactionViewFrom.setConvertedAmount(command.getConvertedAmount());
        }
        transactionViewRepository.save(transactionViewFrom);
    }

}
