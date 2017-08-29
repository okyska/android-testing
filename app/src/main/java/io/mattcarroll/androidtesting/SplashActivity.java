package io.mattcarroll.androidtesting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.mattcarroll.androidtesting.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private static final int LOADER_LAUNCH_WAIT = 1000;

    private final LoaderManager.LoaderCallbacks<Void> launchWaitLoaderCallbacks = new LoaderManager.LoaderCallbacks<Void>() {
        @Override
        public Loader<Void> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "Creating LaunchLoader.");
            return new LaunchLoader(SplashActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {
            Log.d(TAG, "LaunchLoader is done waiting.");
            getSupportLoaderManager().destroyLoader(LOADER_LAUNCH_WAIT);
            launchHomeScreen();
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {
            // no-op.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        waitAndThenLaunchHomeScreen();
    }

    private void waitAndThenLaunchHomeScreen() {
        LoaderManager loaderManager = getSupportLoaderManager();
        if (null == loaderManager.getLoader(LOADER_LAUNCH_WAIT)) {
            Log.d(TAG, "Starting new launch wait Loader.");
        } else {
            Log.d(TAG, "Restoring existing launch wait Loader.");
        }
        loaderManager.initLoader(LOADER_LAUNCH_WAIT, null, launchWaitLoaderCallbacks);
    }

    private void launchHomeScreen() {
        Log.d(TAG, "Launching Home Screen.");
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);

        finish();
    }

    private static class LaunchLoader extends AsyncTaskLoader<Void> {
        public LaunchLoader(Context context) {
            super(context);
            forceLoad();
        }

        @Override
        public Void loadInBackground() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
