package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Credentials that authenticate with a given Bank for a given bank account.
 */
public class AccountCredentials implements Serializable {

    private final String financialInstitutionName;
    private final String accountNumber;
    private final String password;

    public AccountCredentials(@NonNull String financialInstitutionName,
                              @NonNull String accountNumber,
                              @NonNull String password) {
        this.financialInstitutionName = financialInstitutionName;
        this.accountNumber = accountNumber;
        this.password = password;
    }

    public String financialInstitutionName() {
        return financialInstitutionName;
    }

    public String accountNumber() {
        return accountNumber;
    }

    public String password() {
        return password;
    }

}
