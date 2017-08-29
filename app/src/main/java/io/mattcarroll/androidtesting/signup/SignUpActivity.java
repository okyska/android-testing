package io.mattcarroll.androidtesting.signup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.Set;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.usersession.UserSession;

public class SignUpActivity extends AppCompatActivity {
    public static final int RESULT_FAILED = 1001;

    private static final String TAG = "SignUpActivity";
    private static final String KEY_SIGN_UP_FORM = "sign_up_form";

    private SignUpForm signUpForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (null == savedInstanceState) {
            signUpForm = new SignUpForm();
            displayCollectPersonalInfoScreen();
        } else {
            signUpForm = SignUpForm.fromBundle(savedInstanceState.getBundle(KEY_SIGN_UP_FORM));
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_SIGN_UP_FORM, signUpForm.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackBehaviorAllowed()) {
            if (getVisibleFragment() instanceof CollectPersonalInfoFragment) {
                setResult(RESULT_CANCELED);
            }
            super.onBackPressed();
        }
    }

    @Nullable
    private Fragment getVisibleFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.framelayout_container);
    }

    private boolean isBackBehaviorAllowed() {
        return !(getVisibleFragment() instanceof DoSignUpFragment);
    }

    public void onEventMainThread(@NonNull CollectPersonalInfoFragment.PersonalInfoCompletedEvent event) {
        signUpForm.setPersonalInfo(event.getPersonalInfo());
        displayCollectInterestsScreen();
    }

    public void onEventMainThread(@NonNull CollectInterestsFragment.SelectedInterestsCompleteEvent event) {
        signUpForm.setInterests(event.getSelectedInterests());
        displaySelectCredentialsScreen();
    }

    public void onEventMainThread(@NonNull SelectCredentialsFragment.CredentialsSelectedEvent event) {
        signUpForm.setCredentials(event.getCredentials());
        displayDoSignUpScreen();
    }

    public void onEventMainThread(@NonNull DoSignUpFragment.SignUpCompleteEvent event) {
        if (event.isSuccessful()) {
            login();
            finishWithSuccess();
        }
    }

    private void displayCollectPersonalInfoScreen() {
        CollectPersonalInfoFragment screen = CollectPersonalInfoFragment.newInstance();
        doDisplayScreen(screen, false);
    }

    private void displayCollectInterestsScreen() {
        CollectInterestsFragment screen = CollectInterestsFragment.newInstance();
        doDisplayScreen(screen, true);
    }

    private void displaySelectCredentialsScreen() {
        SelectCredentialsFragment screen = SelectCredentialsFragment.newInstance();
        doDisplayScreen(screen, true);
    }

    private void displayDoSignUpScreen() {
        DoSignUpFragment screen = DoSignUpFragment.newInstance(signUpForm);
        doDisplayScreen(screen, false);
    }

    private void doDisplayScreen(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    private void login() {
        UserSession.getInstance().setProfile(
                signUpForm.getCredentials().getUsername(),
                signUpForm.getCredentials().getPassword());
    }

    private void finishWithSuccess() {
        Log.d(TAG, "Finishing SignUpActivity with successful sign-up.");
        setResult(RESULT_OK);
        finish();
    }
}
