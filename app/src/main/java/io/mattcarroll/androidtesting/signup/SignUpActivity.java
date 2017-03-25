package io.mattcarroll.androidtesting.signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.mattcarroll.androidtesting.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (null == savedInstanceState) {
            displayCollectPersonalInfoScreen();
        }
    }

    private void displayCollectPersonalInfoScreen() {
        CollectPersonalInfoFragment screen = CollectPersonalInfoFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.framelayout_container, screen)
                .commit();
    }
}
