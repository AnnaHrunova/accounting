package com.mintos.accounting.exceptions;

public class AccountValidationException extends RuntimeException {
    public AccountValidationException(Reason reason) {
        super(reason.getMessage());
    }

    public AccountValidationException(Reason reason, String arg) {
        super(String.format(reason.getMessage(), arg));
    }
}
