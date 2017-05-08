package io.mattcarroll.androidtesting.signup;

import android.support.annotation.NonNull;

class Credentials {

    private final String username;
    private final String password;

    Credentials(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }
}
