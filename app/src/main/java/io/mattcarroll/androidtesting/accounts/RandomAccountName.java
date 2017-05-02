package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.util.Random;

class RandomAccountName {
    private static final Random RANDOM = new Random();

    private static final String[] ACCOUNT_NAMES = {
            "Chipotle GuacBack™ Rewards",
            "United Ejector Card",
            "BougieBucks Elite",
            "PeakConsumer® Card",
            "NASCARewards",
            "Ultimate Churninator Card",
            "Red Cash Blue Cash",
            "Quintuple Cash Card",
            "Black Label",
            "Fiery Doritos Locos Card",
            "Debt Unlimited Card",
            "QuarterMiles Card",
            "Golden Corral Loyalty Rewards",
            "There's Money Everywhere™ Cash Back",
            "Platinum Diamond Luxe Deluxe",
            "Infinite Points Forever Card",
            "Cash Back Apocalypse Card",
            "No Credit No Problem Card"
    };

    @NonNull
    public String generate() {
        int nameIndex = RANDOM.nextInt(ACCOUNT_NAMES.length);
        return ACCOUNT_NAMES[nameIndex];
    }
}
