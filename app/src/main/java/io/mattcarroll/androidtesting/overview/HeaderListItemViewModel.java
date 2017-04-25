package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ViewModel that presents the header for a Transaction category.
 */
class HeaderListItemViewModel {

    private final String title;
    private final String subtitle;

    public HeaderListItemViewModel(@NonNull String title,
                                   @Nullable String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }
}
