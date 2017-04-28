package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * Masks all but the last 4 digits of the given account number.
 */
class LastFourDigitsMask implements AccountNumberMask {
    private static final int VISIBLE_DIGITS_LENGTH = 4;
    private static final char MASKING_CHARACTER = '*';

    @NonNull
    @Override
    public String apply(@NonNull String accountNumber) {
        final int length = accountNumber.length();
        if (length <= VISIBLE_DIGITS_LENGTH) {
            return accountNumber;
        }

        String lastFourDigits = accountNumber.substring(length - VISIBLE_DIGITS_LENGTH);

        char[] maskedChars = new char[length - VISIBLE_DIGITS_LENGTH];
        Arrays.fill(maskedChars, MASKING_CHARACTER);
        final String maskedString = new String(maskedChars);

        return maskedString + lastFourDigits;
    }
}
