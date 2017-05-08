package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

class BankAccountsDatasource {
    private static BankAccountsDatasource sInstance;

    @NonNull
    public static BankAccountsDatasource getInstance() {
        if (sInstance == null) {
            sInstance = new BankAccountsDatasource();
        }

        return sInstance;
    }

    private Map<String, BankAccount> injectedAccounts = new HashMap<>();

    @VisibleForTesting
    public void injectAccounts(@NonNull BankAccount... accounts) {
        for (BankAccount account : accounts) {
            injectedAccounts.put(account.accountId(), account);
        }
    }

    @NonNull
    public BankAccount linkAccount(@NonNull AccountCredentials credentials) {
        String accountId = credentials.accountNumber();

        BankAccount injectedAccount = injectedAccounts.get(accountId);
        if (injectedAccount != null) {
            return injectedAccount;
        }

        String institutionName = credentials.financialInstitutionName();

        return new BankAccount(
                institutionName,
                new RandomAccountName().generate(),
                credentials.accountNumber(),
                new RandomTransactions().generate());
    }
}
