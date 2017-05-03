package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class LinkAccountLoader extends AsyncTaskLoader<LinkAccountLoader.Result> {

    private final AccountsApi api;
    private final AccountCredentials credentials;

    public LinkAccountLoader(@NonNull Context context,
                             @NonNull AccountsApi api,
                             @NonNull AccountCredentials credentials) {
        super(context);
        this.api = api;
        this.credentials = credentials;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result loadInBackground() {
        try {
            api.linkBankAccount(credentials);
            return new Result(Result.STATUS_SUCCESS);
        } catch (Exception e) {
            return new Result(Result.STATUS_FAILED);
        }
    }

    public static class Result {
        @Retention(SOURCE)
        @IntDef({STATUS_SUCCESS, STATUS_FAILED})
        public @interface StatusCode {}
        public static final int STATUS_SUCCESS = 0;
        public static final int STATUS_FAILED = 1;

        @StatusCode
        private final int statusCode;

        public Result(@StatusCode int statusCode) {
            this.statusCode = statusCode;
        }

        public boolean wasSuccessful() {
            return statusCode == STATUS_SUCCESS;
        }
    }
}
