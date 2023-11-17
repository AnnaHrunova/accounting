package com.mintos.accounting.exceptions;

public class ExternalApiCallException extends RuntimeException {

    public ExternalApiCallException(Reason reason) {
        super(reason.getMessage());
    }

}
