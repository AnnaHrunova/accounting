package com.mintos.accounting.api;

import com.mintos.accounting.api.model.transaction.CreateTransactionRequest;
import com.mintos.accounting.api.model.transaction.CreateTransactionResponse;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.service.TransactionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${accounting.api.v1}/transactions")
@AllArgsConstructor
@CrossOrigin
@Tag(name = "Transactions Operations Controller")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @Operation(summary = "Initiate transaction")
    @PostMapping()
    public ResponseEntity<CreateTransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        val response = transactionsService.createTransaction(request);
        val httpStatus = response.getStatus() == TransactionStatus.ERROR ? HttpStatus.PRECONDITION_FAILED : HttpStatus.CREATED;
        return new ResponseEntity<>(response, httpStatus);
    }
}
