package com.mintos.accounting.api;

import com.mintos.accounting.api.model.CreateAccountRequest;
import com.mintos.accounting.api.model.CreateAccountResponse;
import com.mintos.accounting.api.model.CreateClientRequest;
import com.mintos.accounting.api.model.CreateClientResponse;
import com.mintos.accounting.service.AccountingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${accounting.api.v1}/clients")
@AllArgsConstructor
@CrossOrigin
@Slf4j
public class AccountingController {

    private final AccountingService accountingService;

    @PostMapping()
    public ResponseEntity<CreateClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {
        val response = accountingService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{client_id}/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @PathVariable("client_id") String clientUUID,
            @Valid @RequestBody CreateAccountRequest request) {
        val response = accountingService.createAccount(clientUUID, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
