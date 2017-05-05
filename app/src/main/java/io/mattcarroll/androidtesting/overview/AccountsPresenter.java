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
        String lastFourDigits = presentLastFourDigits(account);
        String balanceAsString = presentBalance(account);
        String amountSpentAsString = presentAmountSpentThisMonth(account);
        String displayName = presentDisplayName(account);

        return new AccountViewModel(account.getAccountId(), displayName,
                lastFourDigits, balanceAsString, amountSpentAsString);
    }

    @NonNull
    private String presentLastFourDigits(@NonNull BankAccount account) {
        int accountNumberLength = account.getAccountId().length();
        return account.getAccountId().substring(accountNumberLength - 4);
    }

    @NonNull
    private String presentBalance(@NonNull BankAccount account) {
        long balanceInCents = account.cashInCents() - account.debtInCents();
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
                account.getTransactionsInDateRange(beginningOfThisMonth(), now());
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
        return account.getBankName() + " " + account.getAccountName();
    }
}
