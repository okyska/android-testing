package io.mattcarroll.androidtesting.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.usersession.UserSession;

/**
 * Select a username and password.
 */
public class SelectCredentialsFragment extends Fragment {

    @NonNull
    public static SelectCredentialsFragment newInstance() {
        return new SelectCredentialsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_credentials, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areCredentialsValid()) {
                    signUp();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Sign Up - Credentials");
    }

    private boolean areCredentialsValid() {
        // TODO:
        return true;
    }

    private void signUp() {
        // TODO:
        Bus.getBus().post(new NextScreenRequestedEvent());
    }
}
