package com.mintos.accounting.service;

import com.mintos.accounting.api.model.transaction.CreateTransactionRequest;
import com.mintos.accounting.service.transaction.CreateTransactionCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {

    CreateTransactionCommand map(CreateTransactionRequest transactionRequest);
}
