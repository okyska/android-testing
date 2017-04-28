package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.mattcarroll.androidtesting.overview.Account;

/**
 * Presents a list of {@link AccountListItemViewModel}s from a set of {@link Account}s.
 */
class AccountListPresenter {
    private final AccountNumberMask mask;
    private final AccountListItemView.OnRemoveClickListener listener;

    public AccountListPresenter(@NonNull AccountNumberMask mask,
                                @Nullable AccountListItemView.OnRemoveClickListener listener) {
        this.mask = mask;
        this.listener = listener;
    }

    @NonNull
    public List<AccountListItemViewModel> present(@NonNull Collection<Account> accounts) {
        List<AccountListItemViewModel> accountViewModels = new ArrayList<>(accounts.size());

        for (Account account : accounts) {
            AccountListItemViewModel viewModel = createViewModel(account, listener);
            accountViewModels.add(viewModel);
        }

        return accountViewModels;
    }

    @NonNull
    private AccountListItemViewModel createViewModel(@NonNull Account account,
                                                     @Nullable AccountListItemView.OnRemoveClickListener listener) {
        String accountNumber = account.getAccountNumber();
        String maskedAccountNumber = mask.apply(accountNumber);
        return new AccountListItemViewModel(
                account.getAccountId(), account.getName(), maskedAccountNumber, listener);
    }
}
