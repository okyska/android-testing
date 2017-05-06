package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class AccountListItemViewModel {
    private final String accountId;
    private final String displayName;
    private final String accountNumber;
    private final AccountListItemView.OnRemoveClickListener listener;

    public AccountListItemViewModel(@NonNull String accountId,
                                    @NonNull String displayName,
                                    @NonNull String accountNumber,
                                    @Nullable AccountListItemView.OnRemoveClickListener listener) {
        this.accountId = accountId;
        this.displayName = displayName;
        this.accountNumber = accountNumber;
        this.listener = listener;
    }

    @NonNull
    public String accountId() {
        return accountId;
    }

    @NonNull
    public String displayName() {
        return displayName;
    }

    @NonNull
    public String accountNumber() {
        return accountNumber;
    }

    @Nullable
    public AccountListItemView.OnRemoveClickListener getOnRemoveClickListener() {
        return listener;
    }
}
