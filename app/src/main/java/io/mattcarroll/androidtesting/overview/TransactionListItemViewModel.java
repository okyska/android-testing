package io.mattcarroll.androidtesting.overview;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import static android.support.annotation.VisibleForTesting.PACKAGE_PRIVATE;

/**
 * ViewModel that presents a Transaction in a list.
 */
@VisibleForTesting(otherwise = PACKAGE_PRIVATE)
public class TransactionListItemViewModel {

    @DrawableRes
    private final Integer icon;
    private final String description;
    private final String amount;

    public TransactionListItemViewModel(@Nullable @DrawableRes Integer icon,
                                        @NonNull String description,
                                        @Nullable String amount) {
        this.icon = icon;
        this.description = description;
        this.amount = amount;
    }

    @Nullable
    public Integer icon() {
        return icon;
    }

    @NonNull
    public String description() {
        return description;
    }

    @Nullable
    public String amount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionListItemViewModel{" +
                "icon=" + icon +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
