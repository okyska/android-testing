package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure representing a bank account.
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
    public String bankName() {
        return bankName;
    }

    @NonNull
    public String accountName() {
        return accountName;
    }

    @NonNull
    public String accountId() {
        return accountId;
    }

    public long cashInCents() {
        int cashInCents = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isCredit()) {
                cashInCents += transaction.amountInCents();
            }
        }
        return cashInCents;
    }

    public long debtInCents() {
        int debt = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isDebit()) {
                debt += Math.abs(transaction.amountInCents());
            }
        }
        return debt;
    }

    @NonNull
    public List<Transaction> allTransactions() {
        return new ArrayList<>(transactions);
    }

    @NonNull
    public List<Transaction> transactionsInDateRange(long startTime, long endTime) {
        ArrayList<Transaction> transactionsInRange = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.date() >= startTime && transaction.date() <= endTime) {
                transactionsInRange.add(transaction);
            }
        }
        return transactionsInRange;
    }

    public boolean sameTransactionHistory(@NonNull BankAccount other) {
        return transactions.containsAll(other.allTransactions())
                && other.allTransactions().containsAll(transactions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        if (!bankName.equals(that.bankName)) return false;
        if (!accountName.equals(that.accountName)) return false;
        return accountId.equals(that.accountId);

    }

    @Override
    public int hashCode() {
        int result = bankName.hashCode();
        result = 31 * result + accountName.hashCode();
        result = 31 * result + accountId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Bank Account. Bank: " + bankName + ". Account Name: " + accountName;
    }
}
