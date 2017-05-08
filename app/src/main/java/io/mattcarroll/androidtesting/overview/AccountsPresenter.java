package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.mattcarroll.androidtesting.accounts.BankAccount;
import io.mattcarroll.androidtesting.accounts.Transaction;

/**
 * Presents a list of {@link AccountViewModel}s from a set of {@link BankAccount}s.
 */
class AccountsPresenter {
    private static final int NUM_TERMINATING_DIGITS = 4;
    private final NumberFormat currencyFormat;

    public AccountsPresenter(@NonNull NumberFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    @NonNull
    public List<AccountViewModel> present(@NonNull Set<BankAccount> accounts) {
        List<AccountViewModel> accountViewModels = new ArrayList<>(accounts.size());

        for (BankAccount account : accounts) {
            AccountViewModel viewModel = present(account);
            accountViewModels.add(viewModel);
        }

        return accountViewModels;
    }

    @NonNull
    private AccountViewModel present(@NonNull BankAccount account) {
        String terminatingDigits = presentTerminatingDigits(account);
        String balanceAsString = presentBalance(account);
        String amountSpentAsString = presentAmountSpentThisMonth(account);
        String displayName = presentDisplayName(account);
        return new AccountViewModel(account.accountId(), displayName,
                terminatingDigits, balanceAsString, amountSpentAsString);
    }

    @NonNull
    private String presentTerminatingDigits(@NonNull BankAccount account) {
        String accountId = account.accountId();
        int accountNumberLength = accountId.length();
        if (accountNumberLength < NUM_TERMINATING_DIGITS) {
            return accountId;
        } else {
            return accountId.substring(accountId.length() - NUM_TERMINATING_DIGITS);
        }
    }

    @NonNull
    private String presentBalance(@NonNull BankAccount account) {
        long balanceInCents = account.debtInCents() - account.cashInCents();
        balanceInCents = Math.max(0, balanceInCents); // if credits exceed debits, show 0 for balance
        BigDecimal balanceInDollars = new BigDecimal(balanceInCents).movePointLeft(2);
        return currencyFormat.format(balanceInDollars);
    }

    @NonNull
    private String presentAmountSpentThisMonth(@NonNull BankAccount account) {
        long amountSpentInCents = calculateAmountSpentThisMonthInCents(account);
        BigDecimal amountSpentInDollars = new BigDecimal(amountSpentInCents).movePointLeft(2);
        return currencyFormat.format(amountSpentInDollars);
    }

    private long calculateAmountSpentThisMonthInCents(@NonNull BankAccount account) {
        List<Transaction> thisMonthTransactions =
                account.transactionsInDateRange(beginningOfThisMonth(), now());
        long amountSpentThisMonth = 0;
        for (Transaction transaction : thisMonthTransactions) {
            if (transaction.isDebit()) {
                amountSpentThisMonth += Math.abs(transaction.amountInCents());
            }
        }
        return amountSpentThisMonth;
    }

    private long beginningOfThisMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTimeInMillis();
    }

    private long now() {
        return new Date().getTime();
    }

    @NonNull
    private String presentDisplayName(@NonNull BankAccount account) {
        return account.bankName() + " " + account.accountName();
    }
}
