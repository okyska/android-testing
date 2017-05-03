package io.mattcarroll.androidtesting;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;

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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.mattcarroll.androidtesting.EspressoUtils.getError;
import static io.mattcarroll.androidtesting.EspressoUtils.getText;
import static io.mattcarroll.androidtesting.EspressoUtils.setChecked;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;

/**
 * Test suite that demonstrates testing of the Sign-Up flow using a Page pattern.  In this example,
 * each Page is implemented as a static inner class of this test suite to avoid confusing these
 * Page Objects with other tests.  In practice, Page Objects should be defined as standalone Classes
 * so that other tests can make use of them.
 */
public class PagesSignUpTest {

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
        new PersonalInfoPage()
                .enterFirstName("Matt")
                .enterLastName("Carroll")
                .enterAddressLine1("123 Fake Street")
                .enterCity("Palo Alto")
                .enterState("CA")
                .enterZip("94024")
                .next()
                .selectInterests("Football")
                .next()
                .enterUsername("myuser")
                .enterPassword("123456")
                .next();

        // Ensure that this Activity destroyed itself upon successful completion of sign up.
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void userSignUpPersonalInfoVerifyRequiredFieldsAreRequired() {
        // Verify required fields show errors and non-required fields do not.
        PersonalInfoPage page = new PersonalInfoPage();
        page.next();

        assertEquals(resources.getString(R.string.input_error_required), page.getFirstNameError());
        assertEquals(resources.getString(R.string.input_error_required), page.getLastNameError());
        assertEquals(resources.getString(R.string.input_error_required), page.getAddressLine1Error());
        assertEquals(resources.getString(R.string.input_error_required), page.getCityError());
        assertEquals(resources.getString(R.string.input_error_required), page.getStateError());
        assertEquals(resources.getString(R.string.input_error_required), page.getZipError());

        assertTrue(TextUtils.isEmpty(page.getAddressLine2Error()));

        // Fill in personal information and ensure we can move forward after triggering errors.
        page.enterFirstName("Matt")
                .enterLastName("Carroll")
                .enterAddressLine1("123 Fake Street")
                .enterCity("Palo Alto")
                .enterState("CA")
                .enterZip("94024")
                .next();

        page.ensureNotVisible();
    }

    @Test
    public void userSignUpInterestsVerifySelectionRequiredToContinue() {
        new PersonalInfoPage()
                .enterFirstName("Matt")
                .enterLastName("Carroll")
                .enterAddressLine1("123 Fake Street")
                .enterCity("Palo Alto")
                .enterState("CA")
                .enterZip("94024")
                .next()
                .next();

        // Verify that a dialog is displayed with an error message.
        onView(withText(R.string.dialog_select_interests_body)).check(matches(isDisplayed()));
    }

    @Test
    public void userSignUpCredentialsVerifyUsernameAndPasswordAreRequired() {
        new PersonalInfoPage()
                .enterFirstName("Matt")
                .enterLastName("Carroll")
                .enterAddressLine1("123 Fake Street")
                .enterCity("Palo Alto")
                .enterState("CA")
                .enterZip("94024")
                .next()
                .selectInterests("Football")
                .next()
                .next();

        // Verify that credentials are required for sign up.
        onView(withId(R.id.autocompletetextview_email))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_password))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
    }

    @Test
    public void userSignUpVerifyBackWorksOnEachPage() {
        CredentialsPage credentialsPage = new PersonalInfoPage()
                .enterFirstName("Matt")
                .enterLastName("Carroll")
                .enterAddressLine1("123 Fake Street")
                .enterCity("Palo Alto")
                .enterState("CA")
                .enterZip("94024")
                .next()
                .selectInterests("Football")
                .next();

        // We're on the final page.  Now go back to each previous page.
        InterestsPage interestsPage = credentialsPage.back();
        interestsPage.ensureVisible();

        // We're on the interests page.  Go back to the Personal Info page.
        PersonalInfoPage personalInfoPage = interestsPage.back();
        personalInfoPage.ensureVisible();

        // Go back again. We expect the Sign Up Activity to disappear.
        boolean didActivityFinish = false;
        try {
            personalInfoPage.back();
        } catch (NoActivityResumedException e) {
            didActivityFinish = true;
        }
        assertTrue(didActivityFinish);
    }

    private static class PersonalInfoPage {

        public void ensureVisible() {
            onView(withId(R.id.edittext_first_name)).check(matches(isDisplayed()));
        }

        public void ensureNotVisible() {
            onView(withId(R.id.edittext_first_name)).check(doesNotExist());
        }

        @NonNull
        public String getFirstName() {
            return getText(withId(R.id.edittext_first_name));
        }

        @Nullable
        public String getFirstNameError() {
            return getError(withId(R.id.edittext_first_name));
        }

        @NonNull
        public PersonalInfoPage enterFirstName(@NonNull String firstName) {
            onView(withId(R.id.edittext_first_name)).perform(
                    scrollTo(),
                    typeText(firstName));
            return this;
        }

        @NonNull
        public String getLastName() {
            return getText(withId(R.id.edittext_last_name));
        }

        @Nullable
        public String getLastNameError() {
            return getError(withId(R.id.edittext_last_name));
        }

        @NonNull
        public PersonalInfoPage enterLastName(@NonNull String lastName) {
            onView(withId(R.id.edittext_last_name)).perform(
                    scrollTo(),
                    typeText(lastName));
            return this;
        }

        @NonNull
        public String getAddressLine1() {
            return getText(withId(R.id.edittext_address_line_1));
        }

        @Nullable
        public String getAddressLine1Error() {
            return getError(withId(R.id.edittext_address_line_1));
        }

        @NonNull
        public PersonalInfoPage enterAddressLine1(@NonNull String addressLine1) {
            onView(withId(R.id.edittext_address_line_1)).perform(
                    scrollTo(),
                    typeText(addressLine1));
            return this;
        }

        @NonNull
        public String getAddressLine2() {
            return getText(withId(R.id.edittext_address_line_2));
        }

        @Nullable
        public String getAddressLine2Error() {
            return getError(withId(R.id.edittext_address_line_2));
        }

        @NonNull
        public PersonalInfoPage enterAddressLine2(@NonNull String addressLine2) {
            onView(withId(R.id.edittext_address_line_2)).perform(
                    scrollTo(),
                    typeText(addressLine2));
            return this;
        }

        @NonNull
        public String getCity() {
            return getText(withId(R.id.edittext_address_city));
        }

        @Nullable
        public String getCityError() {
            return getError(withId(R.id.edittext_address_city));
        }

        @NonNull
        public PersonalInfoPage enterCity(@NonNull String city) {
            onView(withId(R.id.edittext_address_city)).perform(
                    scrollTo(),
                    typeText(city));
            return this;
        }

        @NonNull
        public String getState() {
            return getText(withId(R.id.edittext_address_state));
        }

        @Nullable
        public String getStateError() {
            return getError(withId(R.id.edittext_address_state));
        }

        @NonNull
        public PersonalInfoPage enterState(@NonNull String state) {
            onView(withId(R.id.edittext_address_state)).perform(
                    scrollTo(),
                    typeText(state));
            return this;
        }

        @NonNull
        public String getZip() {
            return getText(withId(R.id.edittext_address_zip));
        }

        @Nullable
        public String getZipError() {
            return getError(withId(R.id.edittext_address_zip));
        }

        @NonNull
        public PersonalInfoPage enterZip(@NonNull String zip) {
            onView(withId(R.id.edittext_address_zip)).perform(
                    scrollTo(),
                    typeText(zip));
            return this;
        }

        public void back() {
            pressBack();
        }

        @NonNull
        public InterestsPage next() {
            onView(withId(R.id.button_next)).perform(
                    scrollTo(),
                    click());
            return new InterestsPage();
        }

    }

    private static class InterestsPage {

        public void ensureVisible() {
            onView(withId(R.id.listview_interests)).check(matches(isDisplayed()));
        }

        public void ensureNotVisible() {
            onView(withId(R.id.listview_interests)).check(doesNotExist());
        }

        @NonNull
        public InterestsPage selectInterests(@NonNull String ... interests) {
            for (String interest : interests) {
                onData(is(interest)).perform(setChecked(true));
            }
            return this;
        }

        @NonNull
        public InterestsPage deselectInterests(@NonNull String ... interests) {
            for (String interest : interests) {
                onData(is(interest)).perform(setChecked(false));
            }
            return this;
        }

        @NonNull
        public PersonalInfoPage back() {
            pressBack();
            return new PersonalInfoPage();
        }

        @NonNull
        public CredentialsPage next() {
            onView(withId(R.id.button_next)).perform(click());
            return new CredentialsPage();
        }
    }

    private static class CredentialsPage {

        public void ensureVisible() {
            onView(withId(R.id.autocompletetextview_email)).check(matches(isDisplayed()));
        }

        public void ensureNotVisible() {
            onView(withId(R.id.autocompletetextview_email)).check(doesNotExist());
        }

        @NonNull
        public String getUsername() {
            return getText(withId(R.id.autocompletetextview_email));
        }

        @Nullable
        public String getUsernameError() {
            return getError(withId(R.id.autocompletetextview_email));
        }

        @NonNull
        public CredentialsPage enterUsername(@NonNull String username) {
            onView(withId(R.id.autocompletetextview_email)).perform(
                    scrollTo(),
                    typeText(username));
            return this;
        }

        @NonNull
        public String getPassword() {
            return getText(withId(R.id.edittext_password));
        }

        @Nullable
        public String getPasswordError() {
            return getError(withId(R.id.edittext_password));
        }

        @NonNull
        public CredentialsPage enterPassword(@NonNull String password) {
            onView(withId(R.id.edittext_password)).perform(
                    scrollTo(),
                    typeText(password));
            return this;
        }

        @NonNull
        public InterestsPage back() {
            pressBack();
            return new InterestsPage();
        }

        public void next() {
            onView(withId(R.id.button_next)).perform(
                    scrollTo(),
                    click());
        }
    }

}
