package io.mattcarroll.androidtesting.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class SignUpForm {

    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    private static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    private static final String KEY_CITY = "city";
    private static final String KEY_ZIP = "zip";
    private static final String KEY_STATE = "state";
    private static final String KEY_INTERESTS = "interests";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    static SignUpForm fromBundle(@NonNull Bundle bundle) {
        SignUpForm signUpForm = new SignUpForm();

        if (bundle.containsKey(KEY_FIRST_NAME)) {
            PersonalInfo personalInfo = new PersonalInfo(
                bundle.getString(KEY_FIRST_NAME),
                bundle.getString(KEY_LAST_NAME),
                bundle.getString(KEY_ADDRESS_LINE_1),
                bundle.getString(KEY_ADDRESS_LINE_2),
                bundle.getString(KEY_CITY),
                bundle.getString(KEY_ZIP),
                bundle.getString(KEY_STATE)
            );
            signUpForm.setPersonalInfo(personalInfo);
        }

        if (bundle.containsKey(KEY_INTERESTS)) {
            HashSet<String> interests = new HashSet<>();
            Collections.addAll(interests, bundle.getStringArray(KEY_INTERESTS));
            signUpForm.setInterests(interests);
        }

        if (bundle.containsKey(KEY_EMAIL)) {
            Credentials credentials = new Credentials(
                    bundle.getString(KEY_EMAIL),
                    bundle.getString(KEY_PASSWORD)
            );
            signUpForm.setCredentials(credentials);
        }

        return signUpForm;
    }

    private PersonalInfo personalInfo;
    private Set<String> interests;
    private Credentials credentials;

    SignUpForm() { }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(@Nullable PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Set<String> getInterests() {
        return interests;
    }

    public void setInterests(@Nullable Set<String> interests) {
        this.interests = interests;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (null != personalInfo) {
            bundle.putString(KEY_FIRST_NAME, personalInfo.getFirstName());
            bundle.putString(KEY_LAST_NAME, personalInfo.getLastName());
            bundle.putString(KEY_ADDRESS_LINE_1, personalInfo.getAddressLine1());
            bundle.putString(KEY_ADDRESS_LINE_2, personalInfo.getAddressLine2());
            bundle.putString(KEY_CITY, personalInfo.getCity());
            bundle.putString(KEY_ZIP, personalInfo.getZip());
            bundle.putString(KEY_STATE, personalInfo.getState());
        }

        if (null != interests) {
            bundle.putStringArray(KEY_INTERESTS, interests.toArray(new String[] {}));
        }

        if (null != credentials) {
            bundle.putString(KEY_EMAIL, credentials.getUsername());
            bundle.putString(KEY_PASSWORD, credentials.getPassword());
        }

        return bundle;
    }

}
