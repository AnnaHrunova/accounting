package com.mintos.accounting.api;

import com.mintos.accounting.api.model.FilteredPageRequest;
import com.mintos.accounting.api.model.transaction.TransactionViewResponse;
import com.mintos.accounting.service.TransactionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${accounting.api.v1}/accounts")
@AllArgsConstructor
@CrossOrigin
@Tag(name = "Transaction History Controller")
public class TransactionHistoryController {

    private final TransactionsService transactionsService;

    @Operation(summary = "Retrieve account transaction history")
    @GetMapping("/{account_id}/transactions")
    public ResponseEntity<Page<TransactionViewResponse>> getAccountTransactions(
            @PathVariable("account_id") String accountUUID,
            @ModelAttribute FilteredPageRequest pageRequest) {
        val response = transactionsService.getHistory(accountUUID, pageRequest);
        return ResponseEntity.ok(response);
    }
}
