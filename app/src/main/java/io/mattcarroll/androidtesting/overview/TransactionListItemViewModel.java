package io.mattcarroll.androidtesting.overview;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ViewModel that presents a Transaction in a list.
 */
class TransactionListItemViewModel {

    @DrawableRes
    private final Integer icon;
    private final String title;
    private final String subtitle;
    private final String detail;

    public TransactionListItemViewModel(@Nullable @DrawableRes Integer icon,
                                        @NonNull String title,
                                        @Nullable String detail) {
        this(icon, title, null, detail);
    }

    public TransactionListItemViewModel(@Nullable @DrawableRes Integer icon,
                                        @NonNull String title,
                                        @Nullable String subtitle,
                                        @Nullable String detail) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.detail = detail;
    }

    @Nullable
    public Integer getIcon() {
        return icon;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    @Nullable
    public String getDetail() {
        return detail;
    }
}
