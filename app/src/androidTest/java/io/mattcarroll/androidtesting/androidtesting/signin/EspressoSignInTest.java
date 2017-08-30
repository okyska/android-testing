package io.mattcarroll.androidtesting.androidtesting.signin;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.SplashActivity;
import io.mattcarroll.androidtesting.androidtesting.SignUpActivity;

import static android.R.id.edit;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static io.mattcarroll.androidtesting.R.id.edittext_email;
import static io.mattcarroll.androidtesting.R.id.withText;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Created by oxana on 8/29/2017.
 */

public class EspressoSignInTest {

    @Rule
    public final ActivityTestRule<SplashActivity> activityRule =
            new ActivityTestRule<>(SplashActivity.class, false, true);

//    private Resources resources;
//
//    @Before
//    public void setup(){
//        resources = InstrumentationRegistry.getTargetContext().getResources();
//    }

    @Test
    public void userSignInPersonalInfoVerifyRequiredFieldsAreRequired(){

        onView(withId(R.id.button_sign_in))
                .perform(click());

        onView(withId(R.id.edittext_email))
                .check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.edittext_password))
                .check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void userSignInPersonalInfoVerify() {
        onView(withId(R.id.edittext_email))
                .perform(typeText("@"));
        onView(withId(R.id.edittext_password))
                .perform(typeText("12345"));
        onView(withId(R.id.button_sign_in))
                .perform(click());
        onView(withId(R.id.textview_no_accounts))
               .check(matches(isDisplayed()));

    }
    @Test
    public  void userSignInPersonalInfoVerifyRequiredSymbolInEmailField(){
        onView(withId(R.id.edittext_email))
                .perform(typeText("aaaaa"));
        onView(withId(R.id.button_sign_in))
                .perform(click());
        onView(withId(R.id.edittext_email))
                .check(matches(hasErrorText("This email address is invalid")));
    }
}
