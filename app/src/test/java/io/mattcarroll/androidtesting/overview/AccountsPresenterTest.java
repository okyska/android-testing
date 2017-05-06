package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.mattcarroll.androidtesting.accounts.BankAccount;
import io.mattcarroll.androidtesting.accounts.Transaction;

import static junit.framework.Assert.assertEquals;

public class AccountsPresenterTest {

    private AccountsPresenter presenter;
    private Set<BankAccount> accounts;

    @Before
    public void setup() {
        presenter = new AccountsPresenter(NumberFormat.getCurrencyInstance(Locale.US));
        accounts = new HashSet<>();
    }

    @Test
    public void itPresentsBankAndAccountNameForDisplayName() {
        linkAccount("Acme", "Super Deluxe Card");
        String expectedDisplayName = "Acme Super Deluxe Card";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String displayName = viewModels.get(0).getDisplayName();
        assertEquals(expectedDisplayName, displayName);
    }

    @Test
    public void itPresentsBalanceGivenTransactions() {
        List<Transaction> transactions = transactionsWithAmountsInCents(-100, -100, 100);
        linkAccountWithTransactions(transactions);
        String expectedBalance = "$1.00";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String balance = viewModels.get(0).getBalance();
        assertEquals(expectedBalance, balance);
    }

    @Test
    public void itPresentsZeroBalanceWhenCreditsExceedDebits() {
        List<Transaction> transactions = transactionsWithAmountsInCents(2000, -100);
        linkAccountWithTransactions(transactions);
        String expectedBalance = "$0.00";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String balance = viewModels.get(0).getBalance();
        assertEquals(expectedBalance, balance);
    }

    @Test
    public void itPresentsAmountSpentGivenTransactions() {
        List<Transaction> transactions = transactionsOnTodayWithAmountsInCents(-50, -25, -25);
        linkAccountWithTransactions(transactions);
        String expectedAmountSpent = "$1.00";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String amountSpent = viewModels.get(0).getAmountSpentThisMonth();
        assertEquals(expectedAmountSpent, amountSpent);
    }

    @Test
    public void itPresentsAmountSpentOnlyForThisMonth() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionsOnTodayWithAmountsInCents(-100, -50));
        transactions.addAll(transactionsWithAmountsInCents(-1000, -500));
        linkAccountWithTransactions(transactions);
        String expectedAmountSpent = "$1.50";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String amountSpent = viewModels.get(0).getAmountSpentThisMonth();
        assertEquals(expectedAmountSpent, amountSpent);
    }

    @Test
    public void itPresentsLastFourDigitsOfAccountNumber() {
        linkAccountWithNumber("0000111122223333");
        String expectedDisplayedDigits = "3333";

        List<AccountViewModel> viewModels = presenter.present(accounts);

        String displayedDigits = viewModels.get(0).getAccountLastDigits();
        assertEquals(expectedDisplayedDigits, displayedDigits);
    }

    @NonNull
    private BankAccount linkAccount(@NonNull String bankName, @NonNull String accountName) {
        BankAccount account = new BankAccount(bankName, accountName, "", Collections.<Transaction>emptyList());
        accounts.add(account);
        return account;
    }

    private BankAccount linkAccountWithTransactions(@NonNull List<Transaction> transactions) {
        BankAccount account = new BankAccount("", "", "", transactions);
        accounts.add(account);
        return account;
    }

    @NonNull
    private List<Transaction> transactionsWithAmountsInCents(Integer... amountsInCents) {
        List<Transaction> transactions = new ArrayList<>(amountsInCents.length);
        for (Integer amount : amountsInCents) {
            transactions.add(new Transaction("", amount, 0));
        }
        return transactions;
    }

    @NonNull
    private List<Transaction> transactionsOnTodayWithAmountsInCents(Integer... amountsInCents) {
        List<Transaction> transactions = new ArrayList<>(amountsInCents.length);
        for (Integer amount : amountsInCents) {
            transactions.add(new Transaction("", amount, new Date().getTime()));
        }
        return transactions;
    }

    @NonNull
    private BankAccount linkAccountWithNumber(@NonNull String accountNumber) {
        BankAccount account = new BankAccount("", "", accountNumber, Collections.<Transaction>emptyList());
        accounts.add(account);
        return account;
    }

}
