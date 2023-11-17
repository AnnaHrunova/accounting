package com.mintos.accounting.api.error;

import com.mintos.accounting.exceptions.AccountValidationException;
import com.mintos.accounting.exceptions.TransactionValidationException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.status;

@Data
@Slf4j
@Builder
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({
            IllegalArgumentException.class, AccountValidationException.class, TransactionValidationException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> handleCommonBadRequestErrors(
            final Exception exception) {
        log.warn("Bad request", exception);
        return status(BAD_REQUEST).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestValidationResponse> handleRestValidationException(MethodArgumentNotValidException ex) {
        var result =
                buildValidationResponse(ex.getFieldErrors());
        return status(HttpStatus.BAD_REQUEST).body(result);
    }

    private RestValidationResponse buildValidationResponse(List<FieldError> fieldValidationErrors) {
        var errorPayload =
                RestValidationResponse.builder()
                        .code(HttpStatus.BAD_REQUEST)
                        .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .fieldValidationErrors(fieldValidationErrors.stream().map(f -> new FieldValidationError(f.getField(), f.getDefaultMessage())).collect(Collectors.toList()));

        return errorPayload.build();
    }
}
