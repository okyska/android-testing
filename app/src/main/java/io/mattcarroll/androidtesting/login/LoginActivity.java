package io.mattcarroll.androidtesting.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.accounts.AccountPersistenceService;
import io.mattcarroll.androidtesting.androidtesting.SignUpActivity;

/**
 * Launch-point for a login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final int RESULT_SIGN_UP_FAILED = 1001;

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
                loadUsersBankAccounts();
                setResult(RESULT_OK);
                showSuccessDialog();
            } else if (SignUpActivity.RESULT_FAILED == resultCode) {
                setResult(RESULT_SIGN_UP_FAILED);
                showFailureDialog();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_signup_successful)
                .setMessage(R.string.dialog_message_signup_successful)
                .setNegativeButton(R.string.button_ok, null)
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_signup_failed)
                .setMessage(R.string.dialog_message_signup_failed)
                .setNegativeButton(R.string.button_ok, null)
                .setCancelable(false)
                .create()
                .show();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LoginFragment.LoginSuccessfulEvent event) {
        loadUsersBankAccounts();
        setResult(RESULT_OK);
        finish();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LoginFragment.SignUpSelectedEvent event) {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivityForResult(signUpIntent, REQUEST_SIGN_UP);
    }

    private void loadUsersBankAccounts() {
        AccountPersistenceService.loadAccounts(this);
    }
}

