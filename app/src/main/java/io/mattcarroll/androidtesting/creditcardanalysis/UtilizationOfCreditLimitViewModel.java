package io.mattcarroll.androidtesting.creditcardanalysis;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ViewModel that presents credit used vs credit available across all of a user's
 * credit cards.
 */
public class UtilizationOfCreditLimitViewModel {

    private final int creditUsedInCents;
    private final int creditAvailableInCents;

    public UtilizationOfCreditLimitViewModel(int creditUsedInCents, int creditAvailableInCents) {
        this.creditUsedInCents = creditUsedInCents;
        this.creditAvailableInCents = creditAvailableInCents;
    }

    public int getCreditUsedInCents() {
        return creditUsedInCents;
    }

    public int getCreditAvailableInCents() {
        return creditAvailableInCents;
    }
}
