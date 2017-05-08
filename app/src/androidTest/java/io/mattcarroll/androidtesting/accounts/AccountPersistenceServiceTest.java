package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.fail;

public class AccountPersistenceServiceTest {
    private static final String INTENT_SERVICE_IDLING_RESOURCE_NAME =
            "IntentServiceIdlingResource_AccountPersistenceServiceTest";
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

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        fail("Create and register an IntentServiceIdlingResource so that you can wait for AccountPersistenceService.");
    }

    @After
    public void teardown() {
        fail("Deregister the IntentServiceIdlingResource.");
        fail("Delete AccountPersistenceService's file so you don't mess up other tests.");
        fail("Remove all accounts from AccountsApi so you don't mess up other tests.");
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
        fail("TODO: to emulate linked bank accounts, add the given bankAccounts to the AccountsApi manually.");
    }

    private void saveAccountsToFile() {
        fail("TODO: run the AccountPersistenceService to save the existing accounts to file.");
        fail("TODO: make sure to wait for the AccountPersistenceService to finish");
    }

    private void verifyAccountsFileExists() {
        fail("TODO: check that the file exists that AccountPersistenceService writes to.  Use TEST_FILE_NAME as the file name.");
    }

    private void verifyAccountsFileContainsAccounts(@NonNull BankAccount ... expectedAccounts) throws IOException {
        fail("Read the contents of AccountPersistenceService's file and deserialize to a Set<BankAccount>.");
        fail("Verify that every expected account is included in the actual accounts.");
        fail("Verify that every actual account is included in the expected accounts.");
    }

    private void writeFakeBankAccountsToFile() throws IOException {
        fail("Turn one or more test accounts into a List<BankAccount>, then turn that into JSON.");
        fail("Write the fake bank account JSON to AccountPersistenceService's file.");
    }

    private void loadAccountsFromFile() {
        fail("Run AccountPersistenceService to load accounts from its file.");
        fail("Be sure to wait for AccountPersistenceService to finish.");
    }

    private void verifyFakeAccountsLoaded() {
        fail("Query the accounts in AccountsApi and ensure they are the same fake accounts that you turned into JSON.");
    }

}
