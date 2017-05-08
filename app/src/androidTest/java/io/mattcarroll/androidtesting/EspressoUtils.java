package io.mattcarroll.androidtesting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingPolicy;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class EspressoUtils {

    // Reads text in a TextView (or subclass) and returns it.
    public static String getText(@NonNull Matcher<View> matcher) {
        GetTextViewAction action = new GetTextViewAction();
        onView(matcher).perform(action);
        return action.readText();
    }

    public static class GetTextViewAction implements ViewAction {

        private String text;

        @Nullable
        public String readText() {
            String returnText = text;
            text = null;
            return returnText;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(TextView.class);
        }

        @Override
        public String getDescription() {
            return "getting error from a TextView (or subclass)";
        }

        @Override
        public void perform(UiController uiController, View view) {
            text = ((TextView) view).getText().toString();
        }
    }

    public static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has no error text: ");
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }

    // Reads error in a TextView (or subclass) and returns it.
    public static String getError(@NonNull Matcher<View> matcher) {
        GetErrorViewAction action = new GetErrorViewAction();
        onView(matcher).perform(action);
        return action.readError();
    }

    public static class GetErrorViewAction implements ViewAction {

        private String error;

        @Nullable
        public String readError() {
            String returnError = error;
            error = null;
            return returnError;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(EditText.class);
        }

        @Override
        public String getDescription() {
            return "getting error from an EditText";
        }

        @Override
        public void perform(UiController uiController, View view) {
            CharSequence errorCharSequence = ((EditText) view).getError();
            error = null == errorCharSequence ? "" : errorCharSequence.toString();
        }
    }

    public static ViewAction setChecked(boolean checked) {
        return new SetCheckedTextViewAction(checked);
    }

    public static class SetCheckedTextViewAction implements ViewAction {

        private final boolean checked;
        private final GeneralClickAction generalClickAction;

        public SetCheckedTextViewAction(boolean checked) {
            this.checked = checked;
            this.generalClickAction = new GeneralClickAction(Tap.SINGLE, GeneralLocation.VISIBLE_CENTER, Press.FINGER);
        }

        @Override
        public Matcher<View> getConstraints() {
            return isAssignableFrom(CheckedTextView.class);
        }

        @Override
        public String getDescription() {
            return (checked ? "checking" : "unchecking") + " a CheckedTextView";
        }

        @Override
        public void perform(UiController uiController, View view) {
            CheckedTextView checkedTextView = (CheckedTextView) view;
            if (checked != checkedTextView.isChecked()) {
                generalClickAction.perform(uiController, view);
            }
        }
    }

    // Waits for all registered IdlingResources to be idle before proceeding.  This method respects
    // the timeout in Espresso's Master IdlingPolicy.
    public static void waitForIdle() {
        new EspressoIdler().waitForIdle();
    }

    private static class EspressoIdler {

        private final IdlingPolicy idlingPolicy;
        private final long maxWaitTimeInMillis;
        private final int sleepTimeInMillis;
        private boolean waiting;
        private long startTime;
        private long waitTime;

        EspressoIdler() {
            idlingPolicy = IdlingPolicies.getMasterIdlingPolicy();
            maxWaitTimeInMillis = idlingPolicy.getIdleTimeoutUnit().toMillis(idlingPolicy.getIdleTimeout());
            sleepTimeInMillis = 50;
        }

        private synchronized void waitForIdle() {
            waiting = true;
            startTime = System.currentTimeMillis();
            waitTime = 0;

            while (waiting) {
                waiting = !allResourcesIdle();
                if (waiting) {
                    updateWaitTime();
                    ensureTimeoutNotExceeded(waitTime);
                    sleep();
                }
            }
        }

        private void updateWaitTime() {
            waitTime = System.currentTimeMillis() - startTime;
        }

        private boolean allResourcesIdle() {
            for (IdlingResource idlingResource : Espresso.getIdlingResources()) {
                if(!idlingResource.isIdleNow()) {
                    return false;
                }
            }
            return true;
        }

        private void ensureTimeoutNotExceeded(long waitTime) {
            if (waitTime > maxWaitTimeInMillis) {
                timeoutOnIdlingResources(idlingPolicy);
            }
        }

        private void timeoutOnIdlingResources(@NonNull IdlingPolicy idlingPolicy) {
            List<String> runningResourceNames = new ArrayList<>();
            for (IdlingResource idlingResource : Espresso.getIdlingResources()) {
                if(!idlingResource.isIdleNow()) {
                    runningResourceNames.add(idlingResource.getName());
                }
            }
            idlingPolicy.handleTimeout(runningResourceNames, "IdlingResources took too long to become idle.");
        }

        private void sleep() {
            try {
                Thread.sleep(sleepTimeInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
