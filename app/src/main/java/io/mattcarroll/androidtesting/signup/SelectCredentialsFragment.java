package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;

/**
 * Select a username and password.
 */
public class SelectCredentialsFragment extends Fragment {

    @NonNull
    public static SelectCredentialsFragment newInstance() {
        return new SelectCredentialsFragment();
    }

    private EditText emailEditText;
    private EditText passwordEditText;

    private final View.OnFocusChangeListener requiredFieldValidator = new RequiredFieldOnFocusChangeListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_credentials, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = (AutoCompleteTextView) view.findViewById(R.id.autocompletetextview_email);
        emailEditText.setOnFocusChangeListener(requiredFieldValidator);

        passwordEditText = (EditText) view.findViewById(R.id.edittext_password);
        passwordEditText.setOnFocusChangeListener(requiredFieldValidator);

        view.findViewById(R.id.button_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextSelected();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Sign Up - Credentials");
    }

    private void onNextSelected() {
        if (areCredentialsValid()) {
            signUp();
        } else {
            getFirstInputWithError().requestFocus();
        }
    }

    private boolean areCredentialsValid() {
        return TextUtils.isEmpty(emailEditText.getError())
                && TextUtils.isEmpty(passwordEditText.getError());
    }

    @Nullable
    private EditText getFirstInputWithError() {
        if (!TextUtils.isEmpty(emailEditText.getError())) {
            return emailEditText;
        }

        if (!TextUtils.isEmpty(passwordEditText.getError())) {
            return passwordEditText;
        }

        return null;
    }

    private void signUp() {
        Bus.getBus().post(
                new CredentialsSelectedEvent(
                        new Credentials(
                                emailEditText.getText().toString(),
                                passwordEditText.getText().toString()
                        )));
    }

    static class CredentialsSelectedEvent {

        private final Credentials credentials;

        private CredentialsSelectedEvent(@NonNull Credentials credentials) {
            this.credentials = credentials;
        }

        @NonNull
        public Credentials getCredentials() {
            return credentials;
        }
    }

}
