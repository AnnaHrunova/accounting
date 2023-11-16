package com.mintos.accounting.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Reason reason) {
        super(reason.getMessage());
    }

    public ResourceNotFoundException(Reason reason, String arg) {
        super(String.format(reason.getMessage(), arg));
    }
}
