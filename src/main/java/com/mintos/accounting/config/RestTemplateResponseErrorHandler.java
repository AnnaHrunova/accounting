package com.mintos.accounting.config;

import com.mintos.accounting.exceptions.ExternalApiCallException;
import com.mintos.accounting.exceptions.Reason;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {
        log.error("Error during API call: {}", httpResponse.getStatusCode());
        throw new ExternalApiCallException(Reason.EXTERNAL_API_CALL_ERROR);
    }
}
