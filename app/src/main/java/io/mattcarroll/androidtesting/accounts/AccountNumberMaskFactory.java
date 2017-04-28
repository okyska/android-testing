package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

public class AccountNumberMaskFactory {
    @NonNull
    public static AccountNumberMask provideLastFourDigitsMask() {
        return new LastFourDigitsMask();
    }
}
