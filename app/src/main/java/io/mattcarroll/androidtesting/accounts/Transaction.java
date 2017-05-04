package io.mattcarroll.androidtesting.accounts;

/**
 * TODO
 */
public class Transaction {

    private final int amountInCents;
    private final long processDate;

    public Transaction(int amountInCents, long processDate) {
        this.amountInCents = amountInCents;
        this.processDate = processDate;
    }

    public boolean isDebit() {
        return amountInCents < 0;
    }

    public boolean isCredit() {
        return amountInCents > 0;
    }

    public long getAmountInCents() {
        return amountInCents;
    }

    public long getProcessDate() {
        return processDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amountInCents=" + amountInCents +
                ", processDate=" + processDate +
                '}';
    }
}
