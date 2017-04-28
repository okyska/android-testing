package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class AccountListItemViewModel {
    private final String accountId;
    private final String accountName;
    private final String maskedAccountNumber;
    private final AccountListItemView.OnRemoveClickListener listener;

    public AccountListItemViewModel(@NonNull String accountId,
                                    @NonNull String accountName,
                                    @NonNull String maskedAccountNumber,
                                    @Nullable AccountListItemView.OnRemoveClickListener listener) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.maskedAccountNumber = maskedAccountNumber;
        this.listener = listener;
    }

    @NonNull
    public String getAccountId() {
        return accountId;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }

    @NonNull
    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    @Nullable
    public AccountListItemView.OnRemoveClickListener getOnRemoveClickListener() {
        return listener;
    }
}
