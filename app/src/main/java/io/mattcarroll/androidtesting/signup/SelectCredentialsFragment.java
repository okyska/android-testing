package io.mattcarroll.androidtesting.signup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Select a username and password.
 */
public class SelectCredentialsFragment extends Fragment {

    @NonNull
    public static SelectCredentialsFragment newInstance() {
        return new SelectCredentialsFragment();
    }

}
