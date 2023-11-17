package com.mintos.accounting.service.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.TransactionValidationException;
import com.mintos.accounting.service.account.AccountData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

import static com.mintos.accounting.common.FormattingUtils.toMoney;

public class TransactionRules {
    private static final BiPredicate<AccountData, BigDecimal> POSITIVE_BALANCE = (account, amount) -> (account.getBalance().subtract(amount).compareTo(toMoney(BigDecimal.ZERO)) > 0);
    private static final BiPredicate<AccountData, Currency> TARGET_CURRENCY_ALLOWED = (account, currency) -> account.getCurrency() == currency;
    private static final BiPredicate<AccountData, AccountData> CONVERSION_REQUIRED = (account1, account2) -> account1.getCurrency() != account2.getCurrency();
    private static final BiPredicate<AccountData, AccountData> SAME_ACCOUNT = (account1, account2) -> account1.getId().equals(account2.getId());

    public static void validateTransaction(AccountData from,
                                           AccountData to,
                                           CreateTransactionCommand command,
                                           Set<String> supportedCurrencies) {

        var amount = command.getAmount();
        if (command.getConvertedAmount() != null) {
            amount = command.getConvertedAmount();
        }

        if (!supportedCurrencies.containsAll(List.of(from.getCurrency().name(), to.getCurrency().name()))) {
            throw new TransactionValidationException(Reason.TRANSACTION_UNSUPPORTED_CURRENCY, command.getRequestId());
        }
        if (SAME_ACCOUNT.test(from, to)) {
            throw new TransactionValidationException(Reason.SAME_ACCOUNT_TRANSACTION, command.getRequestId());
        }
        if (CONVERSION_REQUIRED.test(from, to) && command.getConvertedAmount() == null) {
            throw new TransactionValidationException(Reason.FAILED_CURRENCY_CONVERSION, command.getRequestId());
        }
        if (!POSITIVE_BALANCE.test(from, amount)) {
            throw new TransactionValidationException(Reason.INSUFFICIENT_BALANCE, command.getRequestId());
        }
        if (!TARGET_CURRENCY_ALLOWED.test(to, command.getCurrency())) {
            throw new TransactionValidationException(Reason.INVALID_CURRENCY, command.getRequestId());
        }
    }
}
