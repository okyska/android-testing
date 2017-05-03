package io.mattcarroll.androidtesting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static org.hamcrest.Matchers.isA;

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
}
