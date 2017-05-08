package io.mattcarroll.androidtesting.signup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.BuildConfig;
import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;

/**
 * Signs up with the backend service and displays a spinner while sign-up is in progress.
 */
public class DoSignUpFragment extends Fragment {

    private static final String TAG = "DoSignUpFragment";

    private static final String ARG_SIGN_UP_FORM = "sign_up_form";
    private static final int LOADER_LAUNCH_WAIT = 1000;

    @NonNull
    public static DoSignUpFragment newInstance(@NonNull SignUpForm signUpForm) {
        Bundle args = new Bundle();
        args.putBundle(ARG_SIGN_UP_FORM, signUpForm.toBundle());
        DoSignUpFragment frag = new DoSignUpFragment();
        frag.setArguments(args);
        return frag;
    }

    private SignUpForm signUpForm;

    private final LoaderManager.LoaderCallbacks<Void> launchWaitLoaderCallbacks = new LoaderManager.LoaderCallbacks<Void>() {
        @Override
        public Loader<Void> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "Creating LaunchLoader.");
            return new SignUpLoader(getContext(), signUpForm);
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {
            Log.d(TAG, "LaunchLoader is done waiting.");
            getLoaderManager().destroyLoader(LOADER_LAUNCH_WAIT);
            onSignUpSuccessful();
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {
            // no-op.
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        signUpForm = SignUpForm.fromBundle(getArguments().getBundle(ARG_SIGN_UP_FORM));
        waitAndThenCompleteSignUp();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spinner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Signing Up...");
    }

    private void waitAndThenCompleteSignUp() {
        LoaderManager loaderManager = getLoaderManager();
        if (null == loaderManager.getLoader(LOADER_LAUNCH_WAIT)) {
            Log.d(TAG, "Starting new Sign-Up wait Loader.");
        } else {
            Log.d(TAG, "Restoring existing Sign-Up wait Loader.");
        }
        loaderManager.initLoader(LOADER_LAUNCH_WAIT, null, launchWaitLoaderCallbacks);
    }

    private void onSignUpSuccessful() {
        Bus.getBus().post(new SignUpCompleteEvent(true));
    }

    static class SignUpCompleteEvent {

        private final boolean isSuccessful;

        private SignUpCompleteEvent(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        public boolean isSuccessful() {
            return this.isSuccessful;
        }

    }

    private static class SignUpLoader extends Loader<Void> {

        private final CountingIdlingResource countingIdlingResource;
        private final SignUpForm signUpForm;

        SignUpLoader(Context context, @NonNull SignUpForm signUpForm) {
            super(context);
            this.signUpForm = signUpForm;

            if (BuildConfig.DEBUG) {
                countingIdlingResource = new CountingIdlingResource("signup");
                Espresso.registerIdlingResources(countingIdlingResource);
            } else {
                countingIdlingResource = null;
            }

            forceLoad();
        }

        @Override
        protected void onForceLoad() {
            super.onForceLoad();
            executeTask();
        }

        private void executeTask() {
            if (null != countingIdlingResource) {
                countingIdlingResource.increment();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (null != countingIdlingResource) {
                        countingIdlingResource.decrement();
                    }

                    Espresso.unregisterIdlingResources(countingIdlingResource);
                    deliverResult(null);
                }
            }).start();
        }
    }

}
