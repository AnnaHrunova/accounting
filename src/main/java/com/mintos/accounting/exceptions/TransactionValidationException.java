package com.mintos.accounting.exceptions;

public class TransactionValidationException extends RuntimeException {
    public TransactionValidationException(Reason reason) {
        super(reason.getMessage());
    }

    public TransactionValidationException(Reason reason, String arg) {
        super(String.format(reason.getMessage(), arg));
    }
}
