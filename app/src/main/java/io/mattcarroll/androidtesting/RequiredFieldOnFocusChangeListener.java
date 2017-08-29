package io.mattcarroll.androidtesting;

import android.view.View;
import android.widget.EditText;

/**
 * Validates that a required field has content when the field loses focus.
 */
public class RequiredFieldOnFocusChangeListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) v;
            if (editText.getText().length() == 0) {
                editText.setError("Required.");
            }
        }
    }
}
