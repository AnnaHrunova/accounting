package com.mintos.accounting.service.account;

import com.mintos.accounting.api.FilteredPageRequest;
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
import com.mintos.accounting.service.CreateTransactionCommand;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionViewRepository transactionViewRepository;

    @Transactional
    public UUID createTransaction(@Valid CreateTransactionCommand command) {
        val accountFrom = getAccount(command.getFromAccountUUID());
        val accountTo = getAccount(command.getToAccountUUID());

        val accountFromNewBalance = calculateOutgoingBalance(command, accountFrom.getBalance());
        updateAccount(accountFrom, accountFromNewBalance);

        val accountToNewBalance = accountTo.getBalance().add(command.getAmount());
        updateAccount(accountTo, accountToNewBalance);

        return saveTransaction(command, accountFrom, accountTo).getId();
    }

    public Page<TransactionViewEntity> getHistory(String accountUUID, FilteredPageRequest pageRequest) {
        TransactionViewSpecification spec =
                new TransactionViewSpecification(accountUUID);

        return transactionViewRepository.findAll(spec, pageRequest.getPageable());
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

    private TransactionEntity saveTransaction(CreateTransactionCommand command, AccountEntity accountFrom, AccountEntity accountTo) {
        val transaction = new TransactionEntity();
        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);
        transaction.setAmount(command.getAmount());
        transaction.setCurrency(command.getCurrency());

        val savedTransaction = transactionRepository.save(transaction);
        saveView(savedTransaction, accountFrom, TransactionType.OUTGOING);
        saveView(savedTransaction, accountTo, TransactionType.INCOMING);

        return savedTransaction;
    }

    private void saveView(TransactionEntity savedTransaction, AccountEntity accountFrom, TransactionType outgoing) {
        val transactionViewFrom = new TransactionViewEntity();
        transactionViewFrom.setTransaction(savedTransaction);
        transactionViewFrom.setAccount(accountFrom);
        transactionViewFrom.setType(outgoing);
        transactionViewRepository.save(transactionViewFrom);
    }

}
