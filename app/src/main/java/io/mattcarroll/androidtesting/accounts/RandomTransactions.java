package io.mattcarroll.androidtesting.accounts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generates random Transaction lists so that BankAccounts can be hard-coded with fake Transaction
 * information.
 */
class RandomTransactions {

    private static final int MAX_TRANSACTION_COUNT = 100;
    private static final int MAX_TRANSACTION_AMOUNT_IN_CENTS = 50000;
    private static final long MAX_TRANSACTION_DATE_HISTORY_IN_MS = 60L * 24 * 60 * 60 * 1000;

    public List<Transaction> generate() {
        int transactionCount = randomPositiveInt(MAX_TRANSACTION_COUNT);
        List<Transaction> transactions = new ArrayList<>(transactionCount);
        for (int i = 0; i < transactionCount; ++i) {
            Transaction transaction = randomTransaction();
            transactions.add(transaction);
        }
        return transactions;
    }

    private int randomPositiveInt(int maxValue) {
        return (int) (Math.random() * maxValue);
    }

    private Transaction randomTransaction() {
        int transactionAmount = randomInt(MAX_TRANSACTION_AMOUNT_IN_CENTS);
        long transactionDate = randomDate(MAX_TRANSACTION_DATE_HISTORY_IN_MS);
        return new Transaction(transactionAmount, transactionDate);
    }

    private int randomInt(int maxAbsValue) {
        return (int) ((Math.random() - 0.5) * (2 * maxAbsValue));
    }

    private long randomDate(long maxHistoryInMs) {
        return new Date().getTime() - randomPositiveLong(maxHistoryInMs);
    }

    private long randomPositiveLong(long maxValue) {
        return (long) (Math.random() * maxValue);
    }
}
