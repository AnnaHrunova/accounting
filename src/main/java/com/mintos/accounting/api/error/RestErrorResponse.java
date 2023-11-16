package com.mintos.accounting.api.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse {
    private HttpStatus code;

    private String description;

    private int httpStatusCode;
}
