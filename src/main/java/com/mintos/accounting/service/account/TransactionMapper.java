package com.mintos.accounting.service.account;

import com.mintos.accounting.api.model.CreateTransactionRequest;
import com.mintos.accounting.api.model.TransactionViewResponse;
import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.transaction.TransactionEntity;
import com.mintos.accounting.domain.view.TransactionViewEntity;
import com.mintos.accounting.service.CreateTransactionCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionData map(CreateTransactionRequest transactionRequest);

    @Mapping(source = "transaction.currency", target = "currency")
    @Mapping(source = "transaction.amount", target = "amount")
    @Mapping(source = "transaction.createdDate", target = "dateTime")
    TransactionViewResponse map(TransactionViewEntity transactionViewEntity);

    CreateTransactionCommand mapToCommand(CreateTransactionRequest transactionRequest);
}
