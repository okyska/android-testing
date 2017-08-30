package io.mattcarroll.androidtesting.androidtesting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class PersonalInfo {

    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;

    PersonalInfo(@NonNull String firstName,
                 @NonNull String lastName,
                 @NonNull String addressLine1,
                 @Nullable String addressLine2,
                 @NonNull String city,
                 @NonNull String state,
                 @NonNull String zip) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getAddressLine1() {
        return addressLine1;
    }

    @Nullable
    public String getAddressLine2() {
        return addressLine2;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public String getZip() {
        return zip;
    }

}
