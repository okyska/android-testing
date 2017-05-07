package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ViewModel that presents the header for a Transaction category.
 */
class HeaderListItemViewModel {

    private final String title;

    public HeaderListItemViewModel(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String transactionDate() {
        return title;
    }

}
