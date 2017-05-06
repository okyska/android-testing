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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (amountInCents != that.amountInCents) return false;
        if (date != that.date) return false;
        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + amountInCents;
        result = 31 * result + (int) (date ^ (date >>> 32));
        return result;
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
