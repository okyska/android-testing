package io.mattcarroll.androidtesting.accounts;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static io.mattcarroll.androidtesting.EspressoUtils.waitForIdle;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class AccountPersistenceServiceTest {

    private static final String TEST_FILE_NAME = "test-accounts.json";

    private static final long FAKE_DATE_1 = 100;
    private static final long FAKE_DATE_2 = 200;
    private static final long FAKE_DATE_3 = 300;
    private static final long FAKE_DATE_4 = 400;
    private static final long FAKE_DATE_5 = 500;

    private final List<Transaction> TEST_TRANSACTIONS_1 = Arrays.asList(
            new Transaction("Test transaction 1", 25_00, FAKE_DATE_1),
            new Transaction("Test transaction 2", 500_00, FAKE_DATE_2)
    );
    private final BankAccount TEST_ACCOUNT_1 = new BankAccount("test bank", "test account 1", "123456789", TEST_TRANSACTIONS_1);

    private final List<Transaction> TEST_TRANSACTIONS_2 = Arrays.asList(
            new Transaction("Test transaction 3", 325_50, FAKE_DATE_3),
            new Transaction("Test transaction 4", 10_000_00, FAKE_DATE_4),
            new Transaction("Test transaction 5", 5_000_00, FAKE_DATE_5)
    );
    private final BankAccount TEST_ACCOUNT_2 = new BankAccount("test bank", "test account 2", "987654321", TEST_TRANSACTIONS_2);

    private Context appContext;
    private IntentServiceIdlingResource idlingResource;

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        idlingResource = new IntentServiceIdlingResource(appContext);
        registerIdlingResources(idlingResource);
    }

    @After
    public void teardown() {
        unregisterIdlingResources(idlingResource);
        AccountPersistenceService.accountsFile(appContext, TEST_FILE_NAME).delete();
        AccountsApi.getInstance().removeAllAccounts();
    }

    @Test
    public void itPersistsAccountsToFile() throws IOException, TimeoutException {
        linkBankAccounts(TEST_ACCOUNT_1, TEST_ACCOUNT_2);

        saveAccountsToFile();

        verifyAccountsFileExists();
        verifyAccountsFileContainsAccounts(TEST_ACCOUNT_1, TEST_ACCOUNT_2);
    }

    @Test
    public void itLoadsAccountsFromFile() throws IOException {
        writeFakeBankAccountsToFile();

        loadAccountsFromFile();

        verifyFakeAccountsLoaded();
    }

    private void linkBankAccounts(@NonNull BankAccount ... bankAccounts) {
        AccountsApi.getInstance().addAccounts(bankAccounts);
    }

    private void saveAccountsToFile() {
        AccountPersistenceService.saveAccounts(appContext, TEST_FILE_NAME);
        waitForIdle();
    }

    private void verifyAccountsFileExists() {
        File accountsFile = AccountPersistenceService.accountsFile(appContext, TEST_FILE_NAME);
        assertTrue(accountsFile.exists());
    }

    private void verifyAccountsFileContainsAccounts(@NonNull BankAccount ... expectedAccounts) throws IOException {
        File accountsFile = AccountPersistenceService.accountsFile(appContext, TEST_FILE_NAME);
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(accountsFile));
        Set<BankAccount> actualAccounts = gson.fromJson(reader, new TypeToken<Set<BankAccount>>() { }.getType());

        assertEquals(expectedAccounts.length, actualAccounts.size());
        for (BankAccount expectedAccount : expectedAccounts) {
            boolean found = false;
            for (BankAccount actualAccount : actualAccounts) {
                if (expectedAccount.equals(actualAccount)
                        && expectedAccount.sameTransactionHistory(actualAccount)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fail("Expected BankAccount was not found in file: " + expectedAccount);
            }
        }
    }

    private void writeFakeBankAccountsToFile() throws IOException {
        // Collect fake bank accounts and convert to JSON.
        List<BankAccount> fakeAccounts = Collections.singletonList(TEST_ACCOUNT_1);
        String fakeAccountsJson = new Gson().toJson(fakeAccounts);

        // Write fake bank accounts to file.
        File testFile = AccountPersistenceService.accountsFile(appContext, TEST_FILE_NAME);
        BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
        bw.write(fakeAccountsJson);
        bw.close();
    }

    private void loadAccountsFromFile() {
        AccountPersistenceService.loadAccounts(appContext, TEST_FILE_NAME);
        waitForIdle();
    }

    private void verifyFakeAccountsLoaded() {
        BankAccount expectedAccount = TEST_ACCOUNT_1;

        Set<BankAccount> actualBankAccounts = AccountsApi.getInstance().accounts();
        assertEquals(1, actualBankAccounts.size());
        assertEquals(expectedAccount, actualBankAccounts.iterator().next());
        assertTrue(expectedAccount.sameTransactionHistory(actualBankAccounts.iterator().next()));
    }

    private static class IntentServiceIdlingResource implements IdlingResource {

        private Context context;
        private ResourceCallback resourceCallback;

        IntentServiceIdlingResource(@NonNull Context context) {
            this.context = context;
        }

        @Override
        public String getName() {
            return "IntentServiceIdlingResource";
        }

        @Override
        public boolean isIdleNow() {
            boolean idle = !isIntentServiceRunning();
            if (idle && resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        private boolean isIntentServiceRunning() {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (AccountPersistenceService.class.getName().equals(info.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.resourceCallback = callback;
        }
    }

}
