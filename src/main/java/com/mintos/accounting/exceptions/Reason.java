package com.mintos.accounting.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {

    INSUFFICIENT_BALANCE("Insufficient balance for transactionRequestId: %s"),
    INVALID_CURRENCY("Invalid target currency for transactionRequestId: %s"),
    TRANSACTION_UNSUPPORTED_CURRENCY("Unsupported currency for transactionRequestId: %s"),
    SAME_ACCOUNT_TRANSACTION("Transaction to same account not allowed for transactionRequestId: %s"),

    ACCOUNT_UNSUPPORTED_CURRENCY("Unsupported currency for new account creation"),
    CLIENT_NOT_FOUND("Client with id: %s not found"),
    ACCOUNT_NOT_FOUND("Account with id: %s not found"),
    TRANSACTION_NOT_FOUND("Transaction with id: %s not found"),
    FAILED_CURRENCY_CONVERSION("Failed currency conversion"),
    EXTERNAL_API_CALL_ERROR("Error during external API call");

    private final String message;

}
