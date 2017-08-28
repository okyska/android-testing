package io.mattcarroll.androidtesting.signup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.mattcarroll.androidtesting.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by borisgurtovyy on 8/28/17.
 */


public class EspressoSignUpTest {
    @Rule
    public final ActivityTestRule<SignUpActivity> activityRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class , false, true);

    @Test
    public void userSignUpPersonalInfoVerifyRequiredFieldsAreRequired() {
        onView(withId(R.id.button_next))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.edittext_first_name))
                .check(matches(hasErrorText("Required.")));
        onView(withId(R.id.edittext_last_name))
                .check(matches(hasErrorText("Required.")));
        onView(withId(R.id.edittext_address_line_1))
                .check(matches(hasErrorText("Required.")));

        onView(withId(R.id.edittext_address_city))
                .check(matches(hasErrorText("Required.")));
        onView(withId(R.id.edittext_address_state))
                .check(matches(hasErrorText("Required.")));
        onView(withId(R.id.edittext_address_zip))
                .check(matches(hasErrorText("Required.")));
    }




}
