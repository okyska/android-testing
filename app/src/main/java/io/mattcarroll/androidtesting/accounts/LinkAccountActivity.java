package io.mattcarroll.androidtesting.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.RequiredFieldOnFocusChangeListener;

public class LinkAccountActivity extends AppCompatActivity {
    private static final String TAG_LINK_ACCOUNT_FRAGMENT = "DoLinkAccountFragment";

    private EditText bankNameField;
    private EditText accountNumberField;
    private EditText passwordField;
    private Button linkAccountButton;

    private final View.OnFocusChangeListener requiredFieldValidator = new RequiredFieldOnFocusChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_account);

        bankNameField = (EditText) findViewById(R.id.edittext_bank_name);
        bankNameField.setOnFocusChangeListener(requiredFieldValidator);

        accountNumberField = (EditText) findViewById(R.id.edittext_account_number);
        accountNumberField.setOnFocusChangeListener(requiredFieldValidator);

        passwordField = (EditText) findViewById(R.id.edittext_password);
        passwordField.setOnFocusChangeListener(requiredFieldValidator);

        linkAccountButton = (Button) findViewById(R.id.button_link_account);
        linkAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLinkAccountClicked();
            }
        });
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

    private void onLinkAccountClicked() {
        validateInputs();
        if (isInputValid()) {
            linkAccount();
        }
    }

    private void validateInputs() {
        validateRequiredInput(bankNameField);
        validateRequiredInput(accountNumberField);
        validateRequiredInput(passwordField);
    }

    private void validateRequiredInput(@NonNull EditText editText) {
        if (editText.getText().length() == 0) {
            editText.setError(getString(R.string.input_error_required));
        }
    }

    private boolean isInputValid() {
        return TextUtils.isEmpty(bankNameField.getError())
                && TextUtils.isEmpty(accountNumberField.getError())
                && TextUtils.isEmpty(passwordField.getError());
    }


    private void linkAccount() {
        String bankName = bankNameField.getText().toString();
        String accountNumber = accountNumberField.getText().toString();
        String password = passwordField.getText().toString();
        AccountCredentials credentials = new AccountCredentials(bankName, accountNumber, password);
        launchLinkAccountFragment(credentials);
    }

    private void launchLinkAccountFragment(AccountCredentials credentials) {
        DoLinkAccountFragment fragment = DoLinkAccountFragment.newInstance(credentials);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.relativelayout_container, fragment, TAG_LINK_ACCOUNT_FRAGMENT)
                .commit();
    }

    private void removeLinkAccountFragment() {
        Fragment linkAccountFragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_LINK_ACCOUNT_FRAGMENT);

        if (linkAccountFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(linkAccountFragment)
                    .commit();
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LinkAccountSucceededEvent event) {
        finish();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull LinkAccountFailedEvent event) {
        removeLinkAccountFragment();
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_account_link_failed)
                .setMessage(R.string.dialog_message_account_link_failed)
                .create()
                .show();
    }

}
