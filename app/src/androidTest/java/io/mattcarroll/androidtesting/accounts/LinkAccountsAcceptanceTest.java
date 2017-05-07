package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mattcarroll.androidtesting.IntentServiceIdlingResource;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.SplashActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.mattcarroll.androidtesting.EspressoUtils.waitForIdle;

public class LinkAccountsAcceptanceTest {
    private static final String INTENT_SERVICE_IDLING_RESOURCE_NAME =
            "IntentServiceIdlingResource_LinkAccountsAcceptanceTest";
    private static final String VALID_EMAIL = "me@email.com";
    private static final String VALID_PASSWORD = "password";

    private static final String BANK_NAME = "Acme";
    private static final String ACCOUNT_NUMBER = "0000111100001234";
    private static final String ACCOUNT_NUMBER_LAST_DIGITS = "1234";

    @Rule
    public final ActivityTestRule<SplashActivity> splashActivityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    private Context appContext;
    private IntentServiceIdlingResource idlingResource;

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        idlingResource = new IntentServiceIdlingResource(appContext, INTENT_SERVICE_IDLING_RESOURCE_NAME);
        registerIdlingResources(idlingResource);
    }

    @After
    public void teardown() {
        unregisterIdlingResources(idlingResource);
        //noinspection ResultOfMethodCallIgnored
        AccountPersistenceService.accountsFile(appContext).delete();
        AccountsApi.getInstance().removeAllAccounts();
    }

    @Test
    public void whenUserLinksAccountItAppearsInOverview() {
        // After the splash screen, we're prompted to login

        login(VALID_EMAIL, VALID_PASSWORD);

        // After login completes, the Overview screen is shown

        manageAccounts();

        // Now we're on the Manage Accounts screen

        linkAccount(BANK_NAME, ACCOUNT_NUMBER, VALID_PASSWORD);

        // After account linking completes, we're taken back to Manage Accounts

        pressBack();

        // Now we're back to the Overview screen

        verifyAccountIsShown(ACCOUNT_NUMBER_LAST_DIGITS);
    }

    private void login(@NonNull String email, @NonNull String password) {
        onView(withId(R.id.edittext_email))
                .perform(typeText(email));
        onView(withId(R.id.edittext_password))
                .perform(typeText(password));
        onView(withId(R.id.button_sign_in))
                .perform(click());
    }

    private void manageAccounts() {
        onView(withId(R.id.fab_manage_accounts))
                .perform(click());
    }

    private void linkAccount(@NonNull String bankName,
                             @NonNull String accountNumber,
                             @NonNull String validPassword) {
        onView(withId(R.id.button_link_account))
                .perform(click());

        onView(withId(R.id.edittext_bank_name))
                .perform(typeText(bankName));
        onView(withId(R.id.edittext_account_number))
                .perform(typeText(accountNumber));
        onView(withId(R.id.edittext_password))
                .perform(typeText(validPassword));

        onView(withId(R.id.button_link_account))
                .perform(click());

        waitForIdle();
    }

    private void verifyAccountIsShown(@NonNull String accountNumberLastDigits) {
        onView(withId(R.id.textview_account_last_digits))
                .check(matches(withText(accountNumberLastDigits)));
    }
}
