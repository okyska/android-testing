package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

/**
 * ViewModel that presents the header for a Transaction category.
 */
class HeaderListItemViewModel {

    private final String transactionDate;

    public HeaderListItemViewModel(@NonNull String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @NonNull
    public String transactionDate() {
        return transactionDate;
    }

}
