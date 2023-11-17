package com.mintos.accounting.service;

import com.mintos.accounting.api.model.CreateClientRequest;
import com.mintos.accounting.config.BaseIntegrationTest;
import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountingServiceTest extends BaseIntegrationTest {

    @Autowired
    private AccountingService target;

    @Test
    void shouldCreateClient() {
        val request = new CreateClientRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        val result = target.createClient(request);
        assertThat(result.getClientUUID()).isNotNull();
    }


    @Test
    void shouldValidateCreateClientRequest() {
        val request = new CreateClientRequest();
        request.setFirstName("John");
        assertThrows(ConstraintViolationException.class, () -> target.createClient(request));
    }


}