package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

/**
 * Masks an account number while showing some characters,
 * e.g. 123456789012 -> ************9012
 */

public interface AccountNumberMask {
    @NonNull
    String apply(@NonNull String accountNumber);
}
