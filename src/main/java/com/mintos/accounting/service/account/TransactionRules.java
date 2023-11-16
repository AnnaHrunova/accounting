package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.TransactionValidationException;
import lombok.val;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

public class TransactionRules {

    private static final BiPredicate<AccountData, BigDecimal> POSITIVE_BALANCE = (account, amount) -> (account.getBalance().subtract(amount).compareTo(toMoney(BigDecimal.ZERO)) > 0);
    private static final BiPredicate<AccountData, Currency> TARGET_CURRENCY_ALLOWED = (account, currency) -> account.getCurrency() == currency;

    public static void validateTransaction(AccountData from, AccountData to, TransactionData transactionData) {

        var amount = transactionData.getAmount();
        if (transactionData.getConvertedAmount() != null) {
            amount = transactionData.getConvertedAmount();
        }
        if (!POSITIVE_BALANCE.test(from, amount)) {
            throw new TransactionValidationException(Reason.INSUFFICIENT_BALANCE, transactionData.getRequestId());
        }
        if (!TARGET_CURRENCY_ALLOWED.test(to, transactionData.getCurrency())) {
            throw new TransactionValidationException(Reason.INVALID_CURRENCY, transactionData.getRequestId());
        }
    }
}
