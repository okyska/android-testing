package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

/**
 * TODO
 */
public class Transaction {

    private final String description;
    private final int amountInCents;
    private final long date;

    public Transaction(@NonNull String description, int amountInCents, long date) {
        this.description = description;
        this.amountInCents = amountInCents;
        this.date = date;
    }

    @NonNull
    public String description() {
        return description;
    }

    public boolean isDebit() {
        return amountInCents < 0;
    }

    public boolean isCredit() {
        return amountInCents > 0;
    }

    public long amountInCents() {
        return amountInCents;
    }

    public long date() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", amountInCents=" + amountInCents +
                ", date=" + date +
                '}';
    }
}
