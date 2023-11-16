package com.mintos.accounting.api;

import com.mintos.accounting.api.model.*;
import com.mintos.accounting.service.AccountingService;
import com.mintos.accounting.service.TransactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${accounting.api.v1}/transactions")
@AllArgsConstructor
@CrossOrigin
@Slf4j
public class TransactionsController {

    private final TransactionsService transactionsService;

    @PostMapping()
    public ResponseEntity<CreateTransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        val response = transactionsService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
