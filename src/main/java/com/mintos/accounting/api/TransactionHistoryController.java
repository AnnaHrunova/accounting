package com.mintos.accounting.api;

import com.mintos.accounting.api.model.*;
import com.mintos.accounting.service.TransactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${accounting.api.v1}/accounts")
@AllArgsConstructor
@CrossOrigin
@Slf4j
public class TransactionHistoryController {

    private final TransactionsService transactionsService;

    @GetMapping("/{account_id}/transactions")
    public ResponseEntity<Page<TransactionViewResponse>> getAccountTransactions(
            @PathVariable("account_id") String accountUUID,
            @ModelAttribute FilteredPageRequest pageRequest) {
        val response = transactionsService.getHistory(accountUUID, pageRequest);
        return ResponseEntity.ok(response);
    }
}
