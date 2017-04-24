package io.mattcarroll.androidtesting;


import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.mattcarroll.androidtesting.signup.SignUpActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EspressoSignUpTest {

    @Rule
    public final ActivityTestRule<SignUpActivity> activityRule =
            new ActivityTestRule<>(SignUpActivity.class, false, true);

    private Resources mRes;

    @Before
    public void setup() {
        mRes = InstrumentationRegistry.getTargetContext().getResources();
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
    public void userSignUpPersonalInfoVerifyRequiredFieldsAreRequired() {
        // Verify required fields show errors and non-required fields do not.
        onView(withId(R.id.button_next))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.edittext_first_name))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_last_name))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_address_line_1))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_address_line_2))
                .check(matches(hasNoError()));
        onView(withId(R.id.edittext_address_city))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_address_state))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_address_zip))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));

        // Fill in personal information and ensure we can move forward after triggering errors.
        fillInValidPersonalInfo();
        scrollToAndPressNext();

        // Verify we're no longer on the personal info screen.
        onView(withId(R.id.edittext_first_name)).check(doesNotExist());
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
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_password))
                .check(matches(hasErrorText(mRes.getString(R.string.input_error_required))));
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
        onView(withText("Football")).check(matches(isDisplayed()));

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
            onView(withText(interest))
                    .perform(click());
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

    private static Matcher<View> hasNoError() {
        return new EditTextHasNoErrorMatcher();
    }

    private static class EditTextHasNoErrorMatcher extends BoundedMatcher<View, EditText> {

        public EditTextHasNoErrorMatcher() {
            super(EditText.class);
        }

        @Override
        protected boolean matchesSafely(EditText item) {
            return null == item.getError();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has no error text: ");
        }
    }
}
