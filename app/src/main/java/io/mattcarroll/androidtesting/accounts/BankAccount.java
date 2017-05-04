package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class BankAccount {

    private final String bankName;
    private final String accountName;
    private final String accountId;
    private final List<Transaction> transactions;

    public BankAccount(@NonNull String bankName,
                       @NonNull String accountName,
                       @NonNull String accountId,
                       @NonNull List<Transaction> transactions) {
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountId = accountId;
        this.transactions = transactions;
    }

    @NonNull
    public String getBankName() {
        return bankName;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }

    @NonNull
    public String getAccountId() {
        return accountId;
    }

    public long getCashInCents() {
        int cashInCents = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isCredit()) {
                cashInCents += transaction.getAmountInCents();
            }
        }
        return cashInCents;
    }

    public long getDebtInCents() {
        int debt = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isDebit()) {
                debt += Math.abs(transaction.getAmountInCents());
            }
        }
        return debt;
    }

    @NonNull
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    @NonNull
    public List<Transaction> getTransactionsInDateRange(long startTime, long endTime) {
        ArrayList<Transaction> transactionsInRange = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getProcessDate() >= startTime && transaction.getProcessDate() <= endTime) {
                transactionsInRange.add(transaction);
            }
        }
        return transactionsInRange;
    }
}
