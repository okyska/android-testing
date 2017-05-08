package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Presents a list of {@link AccountListItemViewModel}s from a set of {@link BankAccount}s.
 */
class AccountListPresenter {
    private static final int VISIBLE_DIGITS_LENGTH = 4;
    private static final char MASKING_CHARACTER = '*';

    private final AccountListItemView.OnRemoveClickListener listener;

    public AccountListPresenter(@Nullable AccountListItemView.OnRemoveClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    public List<AccountListItemViewModel> present(@NonNull Set<BankAccount> accounts) {
        List<AccountListItemViewModel> accountViewModels = new ArrayList<>(accounts.size());

        for (BankAccount account : accounts) {
            AccountListItemViewModel viewModel = createViewModel(account, listener);
            accountViewModels.add(viewModel);
        }

        return accountViewModels;
    }

    @NonNull
    private AccountListItemViewModel createViewModel(@NonNull BankAccount account,
                                                     @Nullable AccountListItemView.OnRemoveClickListener listener) {
        String accountNumber = account.accountId();
        String displayName = account.bankName() + " " + account.accountName();
        String maskedAccountNumber = mask(accountNumber);
        return new AccountListItemViewModel(
                accountNumber, displayName, maskedAccountNumber, listener);
    }

    @NonNull
    private String mask(@NonNull String accountNumber) {
        final int length = accountNumber.length();
        if (length <= VISIBLE_DIGITS_LENGTH) {
            return accountNumber;
        }

        String lastFourDigits = accountNumber.substring(length - VISIBLE_DIGITS_LENGTH);

        char[] maskedChars = new char[length - VISIBLE_DIGITS_LENGTH];
        Arrays.fill(maskedChars, MASKING_CHARACTER);
        final String maskedString = new String(maskedChars);

        return maskedString + lastFourDigits;
    }
}
