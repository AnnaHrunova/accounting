package com.mintos.accounting.api;

import com.mintos.accounting.api.model.account.*;
import com.mintos.accounting.service.AccountingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Accounting Operations Controller")
@CrossOrigin
@Slf4j
public class AccountingController {

    private final AccountingService accountingService;

    @Operation(summary = "Create new client")
    @PostMapping
    public ResponseEntity<CreateClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {
        val response = accountingService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Create an account for the client client")
    @PostMapping("/{client_id}/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @PathVariable("client_id") String clientUUID,
            @Valid @RequestBody CreateAccountRequest request) {
        val response = accountingService.createAccount(UUID.fromString(clientUUID), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve client accounts")
    @GetMapping("/{client_id}/accounts")
    public ResponseEntity<AccountsDataResponse> getClientAccounts(
            @PathVariable("client_id") String clientUUID) {
        val response = accountingService.getClientAccounts(UUID.fromString(clientUUID));
        return new ResponseEntity<>(new AccountsDataResponse(response), HttpStatus.OK);
    }
}
