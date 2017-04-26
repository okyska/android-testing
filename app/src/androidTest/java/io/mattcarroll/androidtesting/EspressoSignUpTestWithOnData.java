package io.mattcarroll.androidtesting;


import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.signup.SignUpActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;

public class EspressoSignUpTestWithOnData {

    @Rule
    public final ActivityTestRule<SignUpActivity> activityRule =
            new ActivityTestRule<>(SignUpActivity.class, false, true);

    private Resources resources;

    @Before
    public void setup() {
        resources = InstrumentationRegistry.getTargetContext().getResources();
    }

    @Test
    public void userSignUpHappyPath() {
        // Fill in personal info.
        fillInValidPersonalInfo();
        scrollToAndPressNext();

        // Select interests.
        selectInterest("Football");
        pressNext();

        // Choose credentials and sign up.
        enterCredentials("myuser", "123456");
        scrollToAndPressNext();

        // Ensure that this Activity destroyed itself upon successful completion of sign up.
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void userSignUpInterestsVerifySelectionRequiredToContinue() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndPressNext();

        // Try to continue without selecting an interest.
        pressNext();

        // Verify that a dialog is displayed with an error message.
        onView(withText(R.string.dialog_select_interests_body)).check(matches(isDisplayed()));
    }

    @Test
    public void userSignUpCredentialsVerifyUsernameAndPasswordAreRequired() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndPressNext();

        // Select interests.
        selectInterest("Football");
        pressNext();

        // Verify that credentials are required for sign up.
        scrollToAndPressNext();
        onView(withId(R.id.autocompletetextview_email))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_password))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
    }

    @Test
    public void userSignUpVerifyBackWorksOnEachPage() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndPressNext();

        // Select interests.
        selectInterest("Football");
        pressNext();

        // We're on the final page.  Now go back to each previous page.
        pressBack();

        // Verify we're on the interests page
        onData(is("Football")).check(matches(isDisplayed()));

        // Go back again.
        pressBack();

        // Verify we're on the personal info page
        onView(withId(R.id.edittext_first_name)).check(matches(isDisplayed()));

        // Go back again.
        boolean didActivityFinish = false;
        try {
            pressBack();
        } catch (NoActivityResumedException e) {
            didActivityFinish = true;
        }
        assertTrue(didActivityFinish);
    }

    private void fillInValidPersonalInfo() {
        onView(withId(R.id.edittext_first_name)).perform(
                scrollTo(),
                typeText("Matt"));
        onView(withId(R.id.edittext_last_name)).perform(
                scrollTo(),
                typeText("Carroll"));
        onView(withId(R.id.edittext_address_line_1)).perform(
                scrollTo(),
                typeText("123 Fake Street"));
        onView(withId(R.id.edittext_address_city)).perform(
                scrollTo(),
                typeText("Palo Alto"));
        onView(withId(R.id.edittext_address_state)).perform(
                scrollTo(),
                typeText("CA"));
        onView(withId(R.id.edittext_address_zip)).perform(
                scrollTo(),
                typeText("94024"));
    }

    private void selectInterest(@NonNull String ... interests) {
        for (String interest : interests) {
            onData(is(interest)).perform(click());
        }
    }

    private void scrollToAndPressNext() {
        onView(withId(R.id.button_next)).perform(
                scrollTo(),
                click());
    }

    private void pressNext() {
        onView(withId(R.id.button_next))
                .perform(click());
    }

    private void enterCredentials(@NonNull String email, @NonNull String password) {
        onView(withId(R.id.autocompletetextview_email)).perform(
                scrollTo(),
                typeText(email));
        onView(withId(R.id.edittext_password)).perform(
                scrollTo(),
                typeText(password));
    }

}
