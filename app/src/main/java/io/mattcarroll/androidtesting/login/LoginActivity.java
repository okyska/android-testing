package io.mattcarroll.androidtesting.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.signup.SignUpActivity;

/**
 * Launch-point for a login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGN_UP = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, LoginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        Bus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_SIGN_UP == requestCode) {
            if (RESULT_OK == resultCode) {
                setResult(RESULT_OK);
                finish();
            } else {
                // TODO:
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LoginFragment.LoginSuccessfulEvent event) {
        setResult(RESULT_OK);
        finish();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LoginFragment.SignUpSelectedEvent event) {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivityForResult(signUpIntent, REQUEST_SIGN_UP);
    }

}

