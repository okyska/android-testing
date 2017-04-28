package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

/**
 * Credentials that authenticate with a given Bank for a given bank account.
 */
public class AccountCredentials {

    private final String financialInstitutionName;
    private final String accountNumber;
    private final String username;
    private final String password;

    public AccountCredentials(@NonNull String financialInstitutionName,
                              @NonNull String accountNumber,
                              @NonNull String username,
                              @NonNull String password) {
        this.financialInstitutionName = financialInstitutionName;
        this.accountNumber = accountNumber;
        this.username = username;
        this.password = password;
    }

    public String getFinancialInstitutionName() {
        return financialInstitutionName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
