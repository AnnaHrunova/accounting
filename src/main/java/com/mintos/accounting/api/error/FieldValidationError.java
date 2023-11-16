package com.mintos.accounting.api.error;

public record FieldValidationError(String field, String message) {
}
