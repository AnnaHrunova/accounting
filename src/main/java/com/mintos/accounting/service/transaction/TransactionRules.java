package com.mintos.accounting.service.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.TransactionValidationException;
import com.mintos.accounting.service.account.AccountData;

import java.math.BigDecimal;
import java.util.function.BiPredicate;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

public class TransactionRules {

    private static final BiPredicate<AccountData, BigDecimal> POSITIVE_BALANCE = (account, amount) -> (account.getBalance().subtract(amount).compareTo(toMoney(BigDecimal.ZERO)) > 0);
    private static final BiPredicate<AccountData, Currency> TARGET_CURRENCY_ALLOWED = (account, currency) -> account.getCurrency() == currency;

    public static void validateTransaction(AccountData from, AccountData to, CreateTransactionCommand command) {

        var amount = command.getAmount();
        if (command.getConvertedAmount() != null) {
            amount = command.getConvertedAmount();
        }
        if (!POSITIVE_BALANCE.test(from, amount)) {
            throw new TransactionValidationException(Reason.INSUFFICIENT_BALANCE, command.getRequestId());
        }
        if (!TARGET_CURRENCY_ALLOWED.test(to, command.getCurrency())) {
            throw new TransactionValidationException(Reason.INVALID_CURRENCY, command.getRequestId());
        }
    }
}
