package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Presents a list of {@link AccountListItemViewModel}s from a set of {@link BankAccount}s.
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
    public List<AccountListItemViewModel> present(@NonNull Collection<BankAccount> accounts) {
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
        String accountNumber = account.getAccountId();
        String displayName = account.getBankName() + " " + account.getAccountName();
        String maskedAccountNumber = mask.apply(accountNumber);
        return new AccountListItemViewModel(
                accountNumber, displayName, maskedAccountNumber, listener);
    }
}
