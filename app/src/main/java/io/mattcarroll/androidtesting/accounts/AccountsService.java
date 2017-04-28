package io.mattcarroll.androidtesting.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Android {@code Service} that provides an {@link AccountsApi} when bound.
 */
public class AccountsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder(AccountsApi.getInstance());
    }

    public static class LocalBinder extends Binder {

        private final AccountsApi api;

        private LocalBinder(@NonNull AccountsApi api) {
            this.api = api;
        }

        public AccountsApi getApi() {
            return api;
        }
    }
}
