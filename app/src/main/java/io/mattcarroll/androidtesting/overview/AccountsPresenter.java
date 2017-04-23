package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Presents a list of {@link AccountViewModel}s from a set of {@link Account}s.
 */
class AccountsPresenter {

    private final NumberFormat currencyFormat;

    public AccountsPresenter(@NonNull NumberFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    @NonNull
    public List<AccountViewModel> present(@NonNull Set<Account> accounts) {
        List<AccountViewModel> accountViewModels = new ArrayList<>(accounts.size());

        for (Account account : accounts) {
            AccountViewModel viewModel = createViewModel(account);
            accountViewModels.add(viewModel);
        }

        return accountViewModels;
    }

    @NonNull
    private AccountViewModel createViewModel(@NonNull Account account) {
        int accountNumberLength = account.getAccountNumber().length();
        String lastFourDigits = account.getAccountNumber().substring(accountNumberLength - 4);
        String balanceAsString = currencyFormat.format(account.getBalance());
        String amountSpentAsString = currencyFormat.format(account.getAmountSpentThisMonth());

        return new AccountViewModel(
                account.getName(), lastFourDigits, balanceAsString, amountSpentAsString);
    }
}
