package io.mattcarroll.androidtesting.usersession;

import android.support.annotation.NonNull;

/**
 * Represents the the current user.  The current user can be logged in or anonymous.
 */
public class UserSession {

    private static UserSession userSession;

    @NonNull
    public static UserSession getInstance() {
        if (null == userSession) {
            userSession = new UserSession();
        }
        return userSession;
    }

    private String username;
    private String password;

    private UserSession() {
        // Singleton.
    }

    public boolean isLoggedIn() {
        return null != username;
    }

    public void setProfile(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    public void logout() {
        username = null;
        password = null;
    }
}
