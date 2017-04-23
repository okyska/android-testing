package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * Account data structure to be used until the domain model is developed.
 */
public class Account {
    private final String displayName;
    private final String accountNumber;
    private final BigDecimal balance;
    private final BigDecimal amountSpentThisMonth;

    public Account(@NonNull String displayName,
                   @NonNull String accountNumber,
                   @NonNull BigDecimal balance,
                   @NonNull BigDecimal amountSpentThisMonth) {
        this.displayName = displayName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.amountSpentThisMonth = amountSpentThisMonth;
    }

    @NonNull
    public String getName() {
        return displayName;
    }

    @NonNull
    public String getAccountNumber() {
        return accountNumber;
    }

    @NonNull
    public BigDecimal getBalance() {
        return balance;
    }

    @NonNull
    public BigDecimal getAmountSpentThisMonth() {
        return amountSpentThisMonth;
    }
}
