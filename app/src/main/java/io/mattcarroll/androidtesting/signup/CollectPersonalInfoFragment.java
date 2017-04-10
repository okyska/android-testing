package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_personal_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEditText = (EditText) view.findViewById(R.id.edittext_first_name);
        lastNameEditText = (EditText) view.findViewById(R.id.edittext_last_name);
        addressLine1EditText = (EditText) view.findViewById(R.id.edittext_address_line_1);
        addressLine2EditText = (EditText) view.findViewById(R.id.edittext_address_line_2);
        cityEditText = (EditText) view.findViewById(R.id.edittext_address_city);
        stateEditText = (EditText) view.findViewById(R.id.edittext_address_state);
        zipEditText = (EditText) view.findViewById(R.id.edittext_address_zip);

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
        if (isInputValid()) {
            completePersonalInfoScreen();
        }
    }

    private boolean isInputValid() {
        // TODO: validate input
        return true;
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
