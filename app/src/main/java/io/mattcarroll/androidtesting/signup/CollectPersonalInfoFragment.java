package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.RequiredFieldOnFocusChangeListener;

/**
 * Collect personal info about the user.
 */
public class CollectPersonalInfoFragment extends Fragment {

    @NonNull
    public static CollectPersonalInfoFragment newInstance() {
        return new CollectPersonalInfoFragment();
    }

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText addressLine1EditText;
    private EditText addressLine2EditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText zipEditText;

    private final View.OnFocusChangeListener requiredFieldValidator = new RequiredFieldOnFocusChangeListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_personal_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEditText = (EditText) view.findViewById(R.id.edittext_first_name);
        firstNameEditText.setOnFocusChangeListener(requiredFieldValidator);

        lastNameEditText = (EditText) view.findViewById(R.id.edittext_last_name);
        lastNameEditText.setOnFocusChangeListener(requiredFieldValidator);

        addressLine1EditText = (EditText) view.findViewById(R.id.edittext_address_line_1);
        addressLine1EditText.setOnFocusChangeListener(requiredFieldValidator);

        addressLine2EditText = (EditText) view.findViewById(R.id.edittext_address_line_2);

        cityEditText = (EditText) view.findViewById(R.id.edittext_address_city);
        cityEditText.setOnFocusChangeListener(requiredFieldValidator);

        stateEditText = (EditText) view.findViewById(R.id.edittext_address_state);
        stateEditText.setOnFocusChangeListener(requiredFieldValidator);

        zipEditText = (EditText) view.findViewById(R.id.edittext_address_zip);
        zipEditText.setOnFocusChangeListener(requiredFieldValidator);

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextSelected();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar title for this screen.
        getActivity().setTitle("Sign Up - Personal Info");
    }

    private void onNextSelected() {
        validateAllInputs();
        if (isInputValid()) {
            completePersonalInfoScreen();
        } else {
            getFirstInputWithError().requestFocus();
        }
    }

    private void validateAllInputs() {
        validateRequiredInput(firstNameEditText);
        validateRequiredInput(lastNameEditText);
        validateRequiredInput(addressLine1EditText);
        validateRequiredInput(cityEditText);
        validateRequiredInput(stateEditText);
        validateRequiredInput(zipEditText);
    }

    private void validateRequiredInput(@NonNull EditText editText) {
        if (editText.getText().length() == 0) {
            editText.setError(getString(R.string.input_error_required));
        }
    }

    private boolean isInputValid() {
        return TextUtils.isEmpty(firstNameEditText.getError())
                && TextUtils.isEmpty(lastNameEditText.getError())
                && TextUtils.isEmpty(addressLine1EditText.getError())
                && TextUtils.isEmpty(addressLine2EditText.getError())
                && TextUtils.isEmpty(cityEditText.getError())
                && TextUtils.isEmpty(stateEditText.getError())
                && TextUtils.isEmpty(zipEditText.getError());
    }

    @Nullable
    private EditText getFirstInputWithError() {
        if (!TextUtils.isEmpty(firstNameEditText.getError())) {
            return firstNameEditText;
        }

        if (!TextUtils.isEmpty(lastNameEditText.getError())) {
            return lastNameEditText;
        }

        if (!TextUtils.isEmpty(addressLine1EditText.getError())) {
            return addressLine1EditText;
        }

        if (!TextUtils.isEmpty(addressLine2EditText.getError())) {
            return addressLine2EditText;
        }

        if (!TextUtils.isEmpty(cityEditText.getError())) {
            return cityEditText;
        }

        if (!TextUtils.isEmpty(stateEditText.getError())) {
            return stateEditText;
        }

        if (!TextUtils.isEmpty(zipEditText.getError())) {
            return zipEditText;
        }

        return null;
    }

    private void completePersonalInfoScreen() {
        PersonalInfo personalInfo = new PersonalInfo(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                addressLine1EditText.getText().toString(),
                addressLine2EditText.getText().toString(),
                cityEditText.getText().toString(),
                stateEditText.getText().toString(),
                zipEditText.getText().toString()
        );

        Bus.getBus().post(new PersonalInfoCompletedEvent(personalInfo));
    }

    static class PersonalInfoCompletedEvent {

        private final PersonalInfo personalInfo;

        private PersonalInfoCompletedEvent(@NonNull PersonalInfo personalInfo) {
            this.personalInfo = personalInfo;
        }

        @NonNull
        public PersonalInfo getPersonalInfo() {
            return personalInfo;
        }
    }

}
