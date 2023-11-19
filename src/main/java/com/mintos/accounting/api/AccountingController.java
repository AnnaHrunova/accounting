package com.mintos.accounting.api;

import com.mintos.accounting.api.model.*;
import com.mintos.accounting.service.AccountingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        val response = accountingService.createAccount(UUID.fromString(clientUUID), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{client_id}/accounts")
    public ResponseEntity<AccountsDataResponse> getClientAccounts(
            @PathVariable("client_id") String clientUUID) {
        val response = accountingService.getClientAccounts(UUID.fromString(clientUUID));
        return new ResponseEntity<>(new AccountsDataResponse(response), HttpStatus.OK);
    }
}
