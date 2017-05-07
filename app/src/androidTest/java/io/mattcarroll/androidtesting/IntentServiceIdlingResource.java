package io.mattcarroll.androidtesting;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

import io.mattcarroll.androidtesting.accounts.AccountPersistenceService;

public class IntentServiceIdlingResource implements IdlingResource {

    private Context context;
    private ResourceCallback resourceCallback;

    public IntentServiceIdlingResource(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return "IntentServiceIdlingResource";
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !isIntentServiceRunning();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    private boolean isIntentServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AccountPersistenceService.class.getName().equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
