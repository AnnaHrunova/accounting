package com.mintos.accounting.service.transaction;

import com.mintos.accounting.api.model.transaction.CreateTransactionRequest;
import com.mintos.accounting.api.model.transaction.TransactionViewResponse;
import com.mintos.accounting.domain.transaction.TransactionEntity;
import com.mintos.accounting.domain.view.TransactionViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionData map(CreateTransactionRequest transactionRequest);

    TransactionData map(TransactionEntity transactionEntity);

    @Mapping(source = "transaction.currency", target = "currency")
    @Mapping(source = "transaction.amount", target = "amount")
    @Mapping(source = "transaction.createdDate", target = "dateTime")
    @Mapping(source = "transaction.status", target = "status")
    @Mapping(source = "transaction.id", target = "transactionUUID")
    @Mapping(source = "transaction.requestId", target = "requestId")
    TransactionViewResponse map(TransactionViewEntity transactionViewEntity);

    CreateTransactionCommand mapToCommand(CreateTransactionRequest transactionRequest);
}
