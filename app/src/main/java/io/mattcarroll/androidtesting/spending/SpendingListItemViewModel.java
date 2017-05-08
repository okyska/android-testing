package io.mattcarroll.androidtesting.spending;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

class SpendingListItemViewModel {
    private final Drawable categoryIcon;
    private final String categoryName;
    private final String totalSpent;

    SpendingListItemViewModel(@NonNull Drawable categoryIcon,
                              @NonNull String categoryName,
                              @NonNull String totalSpent) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
        this.totalSpent = totalSpent;
    }

    @NonNull
    public Drawable getCategoryIcon() {
        return categoryIcon;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName;
    }

    @NonNull
    public String getTotalSpent() {
        return totalSpent;
    }
}
