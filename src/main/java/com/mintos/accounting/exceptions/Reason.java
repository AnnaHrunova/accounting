package com.mintos.accounting.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {

    INSUFFICIENT_BALANCE("Insufficient balance for transactionRequestId: %s"),
    INVALID_CURRENCY("Invalid target currency for transactionRequestId: %s"),
    CLIENT_NOT_FOUND("Client with id: %s not found"),
    ACCOUNT_NOT_FOUND("Account with id: %s not found"),
    TRANSACTION_NOT_FOUND("Transactionwith id: %s not found");

    private final String message;

}
