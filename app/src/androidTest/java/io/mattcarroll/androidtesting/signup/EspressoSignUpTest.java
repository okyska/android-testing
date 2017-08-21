package io.mattcarroll.androidtesting.signup;


import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.mattcarroll.androidtesting.R;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
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
        EspressoKey underscore = new EspressoKey.Builder()
                .withShiftPressed(true)
                .withKeyCode(KEYCODE_MINUS)
                .build();
        onView(withId(fieldId))
            .perform(scrollTo())
            .perform(click(), pressKey(underscore))   // more useful for strange text views
                // pressKey also accept simple keycodes
            .perform(typeText(textToType));
    }

    @Test
    public void userSignUpVerifyBackWorksOnEachPage() {
        // Fill in personal info.
        scrollToAndFill(R.id.edittext_first_name, "Matt");
        scrollToAndFill(R.id.edittext_last_name, "Carroll");
        scrollToAndFill(R.id.edittext_address_line_1, "123 Fake Street");
        scrollToAndFill(R.id.edittext_address_city, "Palo Alto");
        scrollToAndFill(R.id.edittext_address_state, "CA");
        scrollToAndFill(R.id.edittext_address_zip, "94024");
        scrollToAndTapNext();

        // Select interests.
        onView(withText("Chess"))
                .perform(click());
        tapNext();

        // We're on the final page.  Now go back to each previous page.
        pressBackOnActivity();

        // Verify we're on the interests page
        onView(withText("Football")).check(matches(isDisplayed()));

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

    private void checkFieldHasError(int fieldId, int errorId) {
        onView(withId(fieldId))
                .check(matches(hasErrorText(resources.getString(errorId))));
    }
}
