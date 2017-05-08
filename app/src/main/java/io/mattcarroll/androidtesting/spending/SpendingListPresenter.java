package io.mattcarroll.androidtesting.spending;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

class SpendingListPresenter {

    @NonNull
    List<SpendingListItemViewModel> presentList(@NonNull Resources resources) {
        Drawable arbitraryIcon = resources.getDrawable(android.R.drawable.ic_delete);

        return Arrays.asList(
                new SpendingListItemViewModel(arbitraryIcon, "Housing", "2500"),
                new SpendingListItemViewModel(arbitraryIcon, "Travel", "1407"),
                new SpendingListItemViewModel(arbitraryIcon, "Fun", "1324"),
                new SpendingListItemViewModel(arbitraryIcon, "Utilities", "1212"),
                new SpendingListItemViewModel(arbitraryIcon, "Dining Out", "1150"),
                new SpendingListItemViewModel(arbitraryIcon, "Transport", "1121")
        );
    }
}
