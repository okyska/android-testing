package io.mattcarroll.androidtesting;

import android.support.annotation.NonNull;

import de.greenrobot.event.EventBus;

/**
 * Singleton EventBus.
 */
public class Bus {

    private static final EventBus bus = new EventBus();

    @NonNull
    public static EventBus getBus() {
        return bus;
    }

}
