package io.mattcarroll.androidtesting.login;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.annotation.NonNull;
import android.support.test.espresso.intent.OngoingStubbing;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.signup.SignUpActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EspressoLoginIntentsTest {
    private static final Instrumentation.ActivityResult SUCCESS =
            createResultWithCode(Activity.RESULT_OK);
    private static final Instrumentation.ActivityResult SIGN_UP_FAILED =
            createResultWithCode(LoginActivity.RESULT_SIGN_UP_FAILED);
    private static final Instrumentation.ActivityResult CANCELLED =
            createResultWithCode(Activity.RESULT_CANCELED);

    @Rule
    public final IntentsTestRule<LoginActivity> intentsRule =
            new IntentsTestRule<>(LoginActivity.class, false, true);

    @Test
    public void loginActivityShouldLaunchSignUpActivityWhenSignUpPressed() {
        clickSignUp();

        // Verify SignUpActivity intent is fired
        intended(hasComponent(SignUpActivity.class.getName()));
    }

    @Test
    public void loginActivityShouldShowSuccessDialogWhenSignUpSucceeds() {
        intendingActivity(SignUpActivity.class).respondWith(SUCCESS);

        clickSignUp();

        // Verify the success dialog is shown
        onView(withText(R.string.dialog_title_signup_successful))
                .check(matches(isDisplayed()));
        onView(withText(R.string.dialog_message_signup_successful))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginActivityShouldShowFailureDialogWhenSignUpFails() {
        intendingActivity(SignUpActivity.class).respondWith(SIGN_UP_FAILED);

        clickSignUp();

        // Verify the failure dialog is shown
        onView(withText(R.string.dialog_title_signup_failed))
                .check(matches(isDisplayed()));
        onView(withText(R.string.dialog_message_signup_failed))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginActivityShouldShowLoginScreenWhenSignUpCancelled() {
        intendingActivity(SignUpActivity.class).respondWith(CANCELLED);

        clickSignUp();

        // Verify the login screen is now visible
        onView(withId(R.id.email_login_form))
                .check(matches(isDisplayed()));
    }

    @NonNull
    private static Instrumentation.ActivityResult createResultWithCode(int resultCode) {
        return new Instrumentation.ActivityResult(resultCode, null);
    }

    @NonNull
    private OngoingStubbing intendingActivity(Class<?> activityClass) {
        return intending(hasComponent(activityClass.getName()));
    }

    private void clickSignUp() {
        onView(withId(R.id.button_sign_up))
                .perform(scrollTo(), click());
    }
}