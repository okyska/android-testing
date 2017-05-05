package io.mattcarroll.androidtesting.accounts;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class LastFourDigitsMaskTest {
    private static final String LONG_ACCOUNT_NUMBER = "123456789";
    private static final String SHORT_ACCOUNT_NUMBER = "1234";

    private LastFourDigitsMask mask;

    @Before
    public void setup() {
        mask = new LastFourDigitsMask();
    }

    @Test
    public void itMasksAccountNumberWhenLengthGreaterThanFour() {
        String lastFourCharacters = LONG_ACCOUNT_NUMBER.substring(LONG_ACCOUNT_NUMBER.length() - 4);
        String otherCharacters = LONG_ACCOUNT_NUMBER.replace(lastFourCharacters, "");

        String maskedNumber = mask.apply(LONG_ACCOUNT_NUMBER);

        assertTrue(maskedNumber.contains(lastFourCharacters));
        assertFalse(maskedNumber.contains(otherCharacters));
    }

    @Test
    public void itDoesNothingWhenAccountNumberLengthEqualToFour() {
        String maskedNumber = mask.apply(SHORT_ACCOUNT_NUMBER);

        assertEquals(SHORT_ACCOUNT_NUMBER, maskedNumber);
    }
}
