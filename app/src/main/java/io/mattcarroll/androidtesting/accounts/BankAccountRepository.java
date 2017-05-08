package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 */
public class BankAccountRepository {

    private final Map<String, BankAccount> bankAccounts = new ConcurrentHashMap<>();

    @NonNull
    public Set<String> getBankAccountIds() {
        return new HashSet<>(bankAccounts.keySet());
    }

    @Nullable
    public BankAccount getBankAccount(@NonNull String accountId) {
        return bankAccounts.get(accountId);
    }

    public void addBankAccounts(@NonNull BankAccount ... newAccounts) {
        for (BankAccount bankAccount : newAccounts) {
            bankAccounts.put(bankAccount.accountId(), bankAccount);
        }
    }

    public boolean removeBankAccount(@NonNull String bankAccountId) {
        BankAccount removedAccount = bankAccounts.remove(bankAccountId);
        return null != removedAccount;
    }

    public void removeAllBankAccounts() {
        bankAccounts.clear();
    }

    @NonNull
    public Set<BankAccount> getBankAccounts() {
        return new HashSet<>(bankAccounts.values());
    }
}
