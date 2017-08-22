package io.mattcarroll.androidtesting.signup;


import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.usersession.UserSession;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.view.KeyEvent.KEYCODE_MINUS;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class EspressoSignUpTest {
    @Rule
    public final ActivityTestRule<SignUpActivity> activityRule =
            new ActivityTestRule<>(SignUpActivity.class, false, true);

    private Resources resources;

    @Before
    public void setup() {
        // getTargetContext() operates on the application under test
        // getContext() operates on the test APK context
        resources = InstrumentationRegistry.getTargetContext().getResources();
    }

    @After
    public void teardown() {
        UserSession.getInstance().logout();
    }

    private static void scrollToAndTapNext() {
        onView(withId(R.id.button_next)).perform(
                scrollTo(),
                click());
    }

    private static void tapNext() {
        onView(withId(R.id.button_next)).perform(
                click());
    }

    private static void scrollToAndFill(int fieldId, String textToType) {
/*        EspressoKey underscore = new EspressoKey.Builder()
                .withShiftPressed(true)
                .withKeyCode(KEYCODE_MINUS)
                .build();*/
        onView(withId(fieldId))
                .perform(scrollTo())
//                .perform(click(), pressKey(underscore))   // more useful for strange text views
                // pressKey also accept simple keycodes
                .perform(typeText(textToType));
    }

    private void checkFieldHasError(int fieldId, int errorId) {
        onView(withId(fieldId))
                .check(matches(hasErrorText(resources.getString(errorId))));
    }

    @Test
    public void userSignUpVerifyBackWorksOnEachPage() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndTapNext();

        // Select interests.
        selectInterest("Astronomy");
        tapNext();

        // We're on the final page.  Now go back to each previous page.
        pressBackOnActivity();

        // Verify we're on the interests page
        onData(is("Astronomy")).check(matches(isDisplayed()));

        // Go back again.
        pressBackOnActivity();

        // Verify we're on the personal info page
        onView(withId(R.id.edittext_first_name)).check(matches(isDisplayed()));

        // Go back again.
        boolean didActivityFinish = false;
        try {
            pressBackOnActivity();
        } catch (NoActivityResumedException e) {
            didActivityFinish = true;
        }
        assertTrue(didActivityFinish);
    }

    private static void pressBackOnActivity() {
        // when soft keyboard is open, back button will just close it
        // but we want to close current fragment/activity instead
        closeSoftKeyboard();
        pressBack();
    }

    private static void selectInterest(String ... interests) {
        for (String interest : interests) {
            onData(is(interest))
                    .perform(click());
        }
    }

    private static void enterCredentials(String email, String password) {
        onView(withId(R.id.autocompletetextview_email)).perform(
                scrollTo(),
                typeText(email));
        onView(withId(R.id.edittext_password)).perform(
                scrollTo(),
                typeText(password));
    }

    @Test
    public void userSignUpPersonalInfoVerifyRequiredFieldsAreRequired() {
        // Verify required fields show errors and non-required fields do not.
        scrollToAndTapNext();

        checkFieldHasError(R.id.edittext_first_name, R.string.input_error_required);
        checkFieldHasError(R.id.edittext_last_name, R.string.input_error_required);
        checkFieldHasError(R.id.edittext_address_line_1, R.string.input_error_required);

        checkFieldHasError(R.id.edittext_address_city, R.string.input_error_required);
        checkFieldHasError(R.id.edittext_address_state, R.string.input_error_required);
        checkFieldHasError(R.id.edittext_address_zip, R.string.input_error_required);
    }

    @Test
    public void userSignUpHappyPath() {
        // Fill in personal info.
        fillInValidPersonalInfo();
        scrollToAndTapNext();

        // Select interests.
        selectInterest("Astronomy");
        tapNext();

        // Choose credentials and sign up.
        enterCredentials("myuser", "123456");
        scrollToAndTapNext();
    }

    @Test
    public void userSignUpInterestsVerifySelectionRequiredToContinue() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndTapNext();

        // Try to continue without selecting an interest.
        tapNext();

        // Verify that a dialog is displayed with an error message.
        onView(withText(R.string.dialog_select_interests_body)).check(matches(isDisplayed()));
    }

    @Test
    public void userSignUpCredentialsVerifyUsernameAndPasswordAreRequired() {
        // Fill in personal information.
        fillInValidPersonalInfo();
        scrollToAndTapNext();

        // Select interests.
        selectInterest("Astronomy");
        tapNext();

        // Verify that credentials are required for sign up.
        scrollToAndTapNext();
        onView(withId(R.id.autocompletetextview_email))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_password))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
    }

    private void fillInValidPersonalInfo() {
        scrollToAndFill(R.id.edittext_first_name, "Matt");
        scrollToAndFill(R.id.edittext_last_name, "Carroll");
        scrollToAndFill(R.id.edittext_address_line_1, "123 Fake Street");
        scrollToAndFill(R.id.edittext_address_city, "Palo Alto");
        scrollToAndFill(R.id.edittext_address_state, "CA");
        scrollToAndFill(R.id.edittext_address_zip, "94024");
    }
}