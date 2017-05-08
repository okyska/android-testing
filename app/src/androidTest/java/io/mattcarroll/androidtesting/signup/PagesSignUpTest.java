package io.mattcarroll.androidtesting.signup;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.usersession.UserSession;

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
import static io.mattcarroll.androidtesting.EspressoUtils.hasNoErrorText;
import static io.mattcarroll.androidtesting.EspressoUtils.setChecked;
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

    @After
    public void teardown() {
        UserSession.getInstance().logout();
    }

    @Test
    public void userSignUpHappyPath() {
        new PersonalInfoPage()
                .firstName("Matt")
                .lastName("Carroll")
                .addressLine1("123 Fake Street")
                .city("Palo Alto")
                .state("CA")
                .zip("94024")
                .next()
                .selectInterests("Football")
                .next()
                .username("myuser")
                .password("123456")
                .next();

        // Ensure that this Activity destroyed itself upon successful completion of sign up.
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void personalInfoScreenRequiredFieldsAreRequired() {
        final String NO_ERROR = "";
        final String REQUIRED_FIELD_ERROR = resources.getString(R.string.input_error_required);

        // Verify required fields show errors and non-required fields do not.
        PersonalInfoPage page = new PersonalInfoPage();

        page.next(); // trigger form errors.

        page.hasFirstNameError(REQUIRED_FIELD_ERROR)
                .hasLastNameError(REQUIRED_FIELD_ERROR)
                .hasAddressLine1Error(REQUIRED_FIELD_ERROR)
                .hasAddressLine2Error(NO_ERROR)
                .hasCityError(REQUIRED_FIELD_ERROR)
                .hasStateError(REQUIRED_FIELD_ERROR)
                .hasZipError(REQUIRED_FIELD_ERROR);

        // Fill in personal information and ensure we can move forward after triggering errors.
        page.firstName("Matt")
                .lastName("Carroll")
                .addressLine1("123 Fake Street")
                .city("Palo Alto")
                .state("CA")
                .zip("94024")
                .next();

        page.isNotVisible();
    }

    @Test
    public void atLeastOneSelectedInterestRequiredToPassInterestsScreen() {
        new PersonalInfoPage()
                .firstName("Matt")
                .lastName("Carroll")
                .addressLine1("123 Fake Street")
                .city("Palo Alto")
                .state("CA")
                .zip("94024")
                .next()
                .next();

        // Verify that a dialog is displayed with an error message.
        onView(withText(R.string.dialog_select_interests_body)).check(matches(isDisplayed()));
    }

    @Test
    public void userSignUpCredentialsVerifyUsernameAndPasswordAreRequired() {
        final String REQUIRED_FIELD_ERROR = resources.getString(R.string.input_error_required);

        CredentialsPage credentialsPage = new PersonalInfoPage()
                .firstName("Matt")
                .lastName("Carroll")
                .addressLine1("123 Fake Street")
                .city("Palo Alto")
                .state("CA")
                .zip("94024")
                .next()
                .selectInterests("Football")
                .next();

        credentialsPage.next(); // trigger form errors.

        // Verify that credentials are required for sign up.
        credentialsPage.hasUsernameError(REQUIRED_FIELD_ERROR)
                .hasPasswordError(REQUIRED_FIELD_ERROR);
    }

    @Test
    public void backWorksOnEachPage() {
        CredentialsPage credentialsPage = new PersonalInfoPage()
                .firstName("Matt")
                .lastName("Carroll")
                .addressLine1("123 Fake Street")
                .city("Palo Alto")
                .state("CA")
                .zip("94024")
                .next()
                .selectInterests("Football")
                .next();

        // We're on the final page.  Now go back to each previous page.
        InterestsPage interestsPage = credentialsPage.back();
        interestsPage.isVisible();

        // We're on the interests page.  Go back to the Personal Info page.
        PersonalInfoPage personalInfoPage = interestsPage.back();
        personalInfoPage.isVisible();

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

        @NonNull
        public PersonalInfoPage isVisible() {
            onView(withId(R.id.edittext_first_name)).check(matches(isDisplayed()));
            return this;
        }

        @NonNull
        public PersonalInfoPage isNotVisible() {
            onView(withId(R.id.edittext_first_name)).check(doesNotExist());
            return this;
        }

        @NonNull
        public PersonalInfoPage hasFirstName(@NonNull String firstName) {
            onView(withId(R.id.edittext_first_name)).check(matches(withText(firstName)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasFirstNameError(@NonNull String error) {
            onView(withId(R.id.edittext_first_name))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage firstName(@NonNull String firstName) {
            onView(withId(R.id.edittext_first_name)).perform(
                    scrollTo(),
                    typeText(firstName));
            return this;
        }

        @NonNull
        public PersonalInfoPage hastLastName(@NonNull String lastName) {
            onView(withId(R.id.edittext_last_name)).check(matches(withText(lastName)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasLastNameError(@NonNull String error) {
            onView(withId(R.id.edittext_last_name))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage lastName(@NonNull String lastName) {
            onView(withId(R.id.edittext_last_name)).perform(
                    scrollTo(),
                    typeText(lastName));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasAddressLine1(@NonNull String addressLine1) {
            onView(withId(R.id.edittext_address_line_1)).check(matches(withText(addressLine1)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasAddressLine1Error(@NonNull String error) {
            onView(withId(R.id.edittext_address_line_1))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage addressLine1(@NonNull String addressLine1) {
            onView(withId(R.id.edittext_address_line_1)).perform(
                    scrollTo(),
                    typeText(addressLine1));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasAddressLine2(@NonNull String addressLine2) {
            onView(withId(R.id.edittext_address_line_2)).check(matches(withText(addressLine2)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasAddressLine2Error(@NonNull String error) {
            onView(withId(R.id.edittext_address_line_2))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage addressLine2(@NonNull String addressLine2) {
            onView(withId(R.id.edittext_address_line_2)).perform(
                    scrollTo(),
                    typeText(addressLine2));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasCity(@NonNull String city) {
            onView(withId(R.id.edittext_address_city)).check(matches(withText(city)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasCityError(@NonNull String error) {
            onView(withId(R.id.edittext_address_city))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage city(@NonNull String city) {
            onView(withId(R.id.edittext_address_city)).perform(
                    scrollTo(),
                    typeText(city));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasState(@NonNull String state) {
            onView(withId(R.id.edittext_address_state)).check(matches(withText(state)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasStateError(@NonNull String error) {
            onView(withId(R.id.edittext_address_state))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage state(@NonNull String state) {
            onView(withId(R.id.edittext_address_state)).perform(
                    scrollTo(),
                    typeText(state));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasZip(@NonNull String zip) {
            onView(withId(R.id.edittext_address_zip)).check(matches(withText(zip)));
            return this;
        }

        @NonNull
        public PersonalInfoPage hasZipError(@NonNull String error) {
            onView(withId(R.id.edittext_address_zip))
                    .check(matches(
                            TextUtils.isEmpty(error) ? hasNoErrorText() : hasErrorText(error)));
            return this;
        }

        @NonNull
        public PersonalInfoPage zip(@NonNull String zip) {
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

        public void isVisible() {
            onView(withId(R.id.listview_interests)).check(matches(isDisplayed()));
        }

        public void isNotVisible() {
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

        public CredentialsPage isVisible() {
            onView(withId(R.id.autocompletetextview_email)).check(matches(isDisplayed()));
            return this;
        }

        public CredentialsPage isNotVisible() {
            onView(withId(R.id.autocompletetextview_email)).check(doesNotExist());
            return this;
        }

        @NonNull
        public CredentialsPage hasUsername(@NonNull String username) {
            onView(withId(R.id.autocompletetextview_email)).check(matches(withText(username)));
            return this;
        }

        @NonNull
        public CredentialsPage hasUsernameError(@NonNull String error) {
            onView(withId(R.id.autocompletetextview_email)).check(matches(hasErrorText(error)));
            return this;
        }

        @NonNull
        public CredentialsPage username(@NonNull String username) {
            onView(withId(R.id.autocompletetextview_email)).perform(
                    scrollTo(),
                    typeText(username));
            return this;
        }

        @NonNull
        public CredentialsPage hasPassword(@NonNull String password) {
            onView(withId(R.id.edittext_password)).check(matches(withText(password)));
            return this;
        }

        @NonNull
        public CredentialsPage hasPasswordError(@NonNull String error) {
            onView(withId(R.id.edittext_password)).check(matches(hasErrorText(error)));
            return this;
        }

        @NonNull
        public CredentialsPage password(@NonNull String password) {
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
