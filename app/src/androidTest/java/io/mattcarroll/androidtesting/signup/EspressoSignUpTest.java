package io.mattcarroll.androidtesting.signup;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    public final ActivityTestRule<SignUpActivity> activityRule =
            new ActivityTestRule<>(SignUpActivity.class, false, true);

    private Resources resources;

    @Before
    public void setup() {
        resources = InstrumentationRegistry.getTargetContext().getResources();
    }


    @Test
    public void userSignUpPersonalInfoVerifyRequiredFieldsAreRequired() {

        onView(withId(R.id.button_next))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.edittext_first_name))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_last_name))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));
        onView(withId(R.id.edittext_address_line_1))
                .check(matches(hasErrorText(resources.getString(R.string.input_error_required))));

    }


}
