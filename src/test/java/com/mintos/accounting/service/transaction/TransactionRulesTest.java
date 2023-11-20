package com.mintos.accounting.service.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.exceptions.Reason;
import com.mintos.accounting.exceptions.TransactionValidationException;
import com.mintos.accounting.service.account.AccountData;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.mintos.accounting.common.FormattingUtils.toMoney;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionRulesTest {

    private static final String TRANSACTION_REQUEST_ID = "Test-Transaction-999";
    private static final UUID TRANSACTION_ID = UUID.fromString("547f0161-2140-4b7f-8ee0-34cb56992bdd");

    @ParameterizedTest
    @MethodSource("invalidTransactionInput")
    void testTransactionValidationRules(AccountData from, AccountData to, CreateTransactionCommand command, Reason expectedReason) {
        val supportedCurrencies = Set.of(Currency.EUR, Currency.GBP);

        val expectedMessage = String.format(expectedReason.getMessage(), TRANSACTION_REQUEST_ID);
        val exception = assertThrows(TransactionValidationException.class, () -> TransactionRules.validateTransaction(from, to, command, supportedCurrencies));
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    static Collection invalidTransactionInput() {
        return Arrays.asList(
                new Object[][]{
                        {accountData(Currency.EUR), accountData(Currency.USD), command(Currency.EUR, 10), Reason.TRANSACTION_UNSUPPORTED_CURRENCY},
                        {accountData(Currency.EUR), accountData(Currency.EUR), command(Currency.USD, 10), Reason.INVALID_CURRENCY},
                        {accountData(Currency.EUR, TRANSACTION_ID), accountData(Currency.EUR, TRANSACTION_ID), command(Currency.EUR, 10), Reason.SAME_ACCOUNT_TRANSACTION},
                        {accountData(Currency.EUR), accountData(Currency.GBP), command(Currency.GBP, 10), Reason.FAILED_CURRENCY_CONVERSION},
                        {accountData(Currency.EUR, 10), accountData(Currency.EUR), command(Currency.EUR, 20), Reason.INSUFFICIENT_BALANCE},
                });
    }

    private static AccountData accountData(Currency currency, UUID... uuid) {
        return AccountData.builder()
                .id(uuid.length == 1 ? uuid[0] : UUID.randomUUID())
                .currency(currency)
                .balance(toMoney(new BigDecimal("100")))
                .build();
    }

    private static AccountData accountData(Currency currency, int amount) {
        val account = accountData(currency);
        account.setBalance(toMoney(new BigDecimal(amount)));
        return account;
    }

    private static CreateTransactionCommand command(Currency currency, int amount) {
        return CreateTransactionCommand.builder()
                .requestId(TRANSACTION_REQUEST_ID)
                .currency(currency)
                .amount(toMoney(BigDecimal.valueOf(amount)))
                .build();
    }

}