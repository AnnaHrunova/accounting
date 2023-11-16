package com.mintos.accounting.api.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestValidationResponse {
    private HttpStatus code;

    private String description;

    private List<FieldValidationError> fieldValidationErrors;
}
