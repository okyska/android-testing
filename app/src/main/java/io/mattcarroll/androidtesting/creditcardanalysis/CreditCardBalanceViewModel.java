package io.mattcarroll.androidtesting.creditcardanalysis;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ViewModel that presents a credit card balance and annualized interest
 * across all of the user's credit cards.  Annualized interest may be
 * unavailable.
 */
public class CreditCardBalanceViewModel {

    private final int totalBalanceInCents;
    private final Integer annualizedInterestInCents;

    public CreditCardBalanceViewModel(int totalBalanceInCents, @Nullable Integer annualizedInterestInCents) {
        this.totalBalanceInCents = totalBalanceInCents;
        this.annualizedInterestInCents = annualizedInterestInCents;
    }

    @NonNull
    public int getTotalBalanceInCents() {
        return totalBalanceInCents;
    }

    // Null means the financial data is missing.
    @Nullable
    public Integer getAnnualizedInterestInCents() {
        return annualizedInterestInCents;
    }
}
