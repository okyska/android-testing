package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

/**
 * View model consumed by {@link AccountView} to display information about an account.
 */
class AccountViewModel {
    private final String displayName;
    private final String accountLastDigits;
    private final String balance;
    private final String amountSpent;

    public AccountViewModel(@NonNull String displayName,
                            @NonNull String accountLastDigits,
                            @NonNull String balance,
                            @NonNull String amountSpent) {
        this.displayName = displayName;
        this.accountLastDigits = accountLastDigits;
        this.balance = balance;
        this.amountSpent = amountSpent;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getAccountLastDigits() {
        return accountLastDigits;
    }

    @NonNull
    public String getBalance() {
        return balance;
    }

    @NonNull
    public String getAmountSpentThisMonth() {
        return amountSpent;
    }
}
