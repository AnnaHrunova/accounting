package com.mintos.accounting.service.account;

import com.mintos.accounting.exceptions.AccountValidationException;
import com.mintos.accounting.exceptions.Reason;

import java.util.Set;

public class AccountRules {
    public static void validateAccount(CreateAccountCommand account, Set<String> supportedCurrencies) {
        if (!supportedCurrencies.contains(account.getCurrency().name())) {
            throw new AccountValidationException(Reason.ACCOUNT_UNSUPPORTED_CURRENCY);
        }
    }
}
