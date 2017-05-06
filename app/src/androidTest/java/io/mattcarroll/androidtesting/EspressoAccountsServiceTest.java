package io.mattcarroll.androidtesting;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import io.mattcarroll.androidtesting.accounts.AccountCredentials;
import io.mattcarroll.androidtesting.accounts.AccountsApi;
import io.mattcarroll.androidtesting.accounts.AccountsService;

public class EspressoAccountsServiceTest {

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    private AccountsApi api;

    @Before
    public void setup() throws TimeoutException {
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), AccountsService.class);
        AccountsService.LocalBinder binder = (AccountsService.LocalBinder) serviceTestRule.bindService(serviceIntent);
        api = binder.getApi();
    }

    @Test
    public void itLinksBankAccounts() {
        AccountCredentials credentials = new AccountCredentials(
                "Bank of America",
                "123456789",
                "testpassword"
        );

        api.linkBankAccount(credentials);

        // Successful execution means the Service is working.
    }

}
