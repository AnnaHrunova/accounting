package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.AccountValidationException;
import com.mintos.accounting.exceptions.Reason;

import java.util.Set;

public class AccountRules {
    public static void validateAccount(CreateAccountCommand account, Set<Currency> supportedCurrencies) {
        if (!supportedCurrencies.contains(account.getCurrency())) {
            throw new AccountValidationException(Reason.ACCOUNT_UNSUPPORTED_CURRENCY);
        }
    }
}
