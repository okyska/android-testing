package io.mattcarroll.androidtesting;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.signup.SignUpActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static junit.framework.Assert.assertTrue;

public class EspressoSignUpTest {

    /**
     * Resource ID guide:
     *
     * Personal Info screen
     *    • CollectPersonalInfoFragment.java
     *    • fragment_collect_personal_info.xml
     *
     *  First name              R.id.edittext_first_name
     *  Last name               R.id.edittext_last_name
     *  Address line 1          R.id.edittext_address_line_1
     *  Address line 2          R.id.edittext_address_line_2
     *  City                    R.id.edittext_address_city
     *  State                   R.id.edittext_address_state
     *  ZIP                     R.id.edittext_address_zip
     *  Next button             R.id.button_next
     *
     * Collect Interests screen
     *    • CollectInterestsFragment.java
     *    • fragment_collect_interests.xml
     *
     *  Next button             R.id.button_next
     *  Error dialog body       R.string.dialog_select_interests_body
     *
     * Credentials screen
     *    • SelectCredentialsFragment.java
     *    • fragment_select_credentials.xml
     *
     *  Email                   R.id.autocompletetextview_email
     *  Password                R.id.edittext_password
     *  Sign Up! button         R.id.button_next
     */

    @Test
    public void userSignUpHappyPath() {
        // TODO implement this test
    }

    @Test
    public void requiredFieldsAreRequired() {
        // TODO implement this test
    }

    @Test
    public void atLeastOneInterestMustBeSelectedToContinue() {
        // TODO implement this test
    }
}
