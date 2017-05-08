package io.mattcarroll.androidtesting.accounts;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import io.mattcarroll.androidtesting.R;

/**
 * Persists Bank Account information to a local file.
 */
public class AccountPersistenceService extends IntentService {

    private static final String TAG = "AccountPersistenceService";

    public static final String EXTRA_ACTION = "action";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_LOAD = "load";
    public static final String EXTRA_FILE_NAME = "file_name";

    public static void saveAccounts(@NonNull Context context) {
        context.startService(new Intent(context, AccountPersistenceService.class)
                .putExtra(EXTRA_ACTION, ACTION_SAVE));
    }

    public static void saveAccounts(@NonNull Context context, @NonNull String filename) {
        context.startService(new Intent(context, AccountPersistenceService.class)
                .putExtra(EXTRA_ACTION, ACTION_SAVE)
                .putExtra(EXTRA_FILE_NAME, filename));
    }

    public static void loadAccounts(@NonNull Context context) {
        context.startService(new Intent(context, AccountPersistenceService.class)
                .putExtra(EXTRA_ACTION, ACTION_LOAD));
    }

    public static void loadAccounts(@NonNull Context context, @NonNull String filename) {
        context.startService(new Intent(context, AccountPersistenceService.class)
                .putExtra(EXTRA_ACTION, ACTION_LOAD)
                .putExtra(EXTRA_FILE_NAME, filename));
    }

    // Static and Visible for testing access.
    static File accountsFile(@NonNull Context context) {
        return accountsFile(context, context.getString(R.string.default_accounts_file_name));
    }

    // Static and Visible for testing access.
    static File accountsFile(@NonNull Context context, @NonNull String filename) {
        return new File(context.getFilesDir(), filename);
    }

    public AccountPersistenceService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null == intent || !intent.hasExtra(EXTRA_ACTION)) {
            return;
        }

        String action = intent.getStringExtra(EXTRA_ACTION);
        String filename = getFileName(intent);
        if (ACTION_SAVE.equals(action)) {
            saveAccountsToFile(filename);
        } else if (ACTION_LOAD.equals(action)) {
            loadAccountsFromFile(filename);
        }
    }

    @NonNull
    private String getFileName(@Nullable Intent intent) {
        return intent != null && intent.hasExtra(EXTRA_FILE_NAME)
                ? intent.getStringExtra(EXTRA_FILE_NAME)
                : getString(R.string.default_accounts_file_name);
    }

    private void saveAccountsToFile(@NonNull String filename) {
        try {
            Log.d(TAG, "Persisting accounts to file: " + filename);
            File accountsFile = accountsFile(this, filename);
            AccountsApi.getInstance().writeAccountsToFile(accountsFile);
            Log.d(TAG, "Done persisting accounts to file.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to persist Accounts.", e);
        }
    }

    private void loadAccountsFromFile(@NonNull String filename) {
        try {
            Log.d(TAG, "Loading accounts from file: " + filename);
            File accountsFile = accountsFile(this, filename);
            AccountsApi.getInstance().loadAccountsFromFile(accountsFile);
            Log.d(TAG, "Done loading accounts from file.");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to load Accounts from file.", e);
        }
    }
}
