package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.SplashActivity;
import io.mattcarroll.androidtesting.overview.TransactionListItemViewModel;
import io.mattcarroll.androidtesting.usersession.UserSession;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class LinkAccountsTest {
    private static final String VALID_EMAIL = "me@email.com";
    private static final String VALID_PASSWORD = "password";

    private static final String BANK_NAME = "Acme";
    private static final String ACCOUNT_NUMBER = "0000111100001234";
    private static final String ACCOUNT_NUMBER_LAST_DIGITS = "1234";
    private static final String ACCOUNT_NAME = "Account 1";
    private static final String DISPLAY_NAME = "Acme Account 1";
    private static final String BALANCE = "$3.50";
    private static final String AMOUNT_SPENT_THIS_MONTH = "$2.00";

    private static final long TODAY = new Date().getTime();
    private static final long YEARS_AGO = 0;

    private static final String TRANSACTION_1_TITLE = "Test transaction 1";
    private static final int TRANSACTION_1_AMOUNT_IN_CENTS = -2_00;
    private static final String TRANSACTION_1_AMOUNT_AS_STRING = "-$2.00";
    private static final Transaction TRANSACTION_1 = new Transaction(TRANSACTION_1_TITLE, TRANSACTION_1_AMOUNT_IN_CENTS, TODAY);

    private static final String TRANSACTION_2_TITLE = "Test transaction 2";
    private static final int TRANSACTION_2_AMOUNT_IN_CENTS = -1_50;
    private static final String TRANSACTION_2_AMOUNT_AS_STRING = "-$1.50";
    private static final Transaction TRANSACTION_2 = new Transaction(TRANSACTION_2_TITLE, TRANSACTION_2_AMOUNT_IN_CENTS, YEARS_AGO);
    private static final List<Transaction> TEST_TRANSACTIONS = Arrays.asList(
            TRANSACTION_1, TRANSACTION_2
    );
    private static final BankAccount TEST_ACCOUNT =
            new BankAccount(BANK_NAME, ACCOUNT_NAME, ACCOUNT_NUMBER, TEST_TRANSACTIONS);

    @Rule
    public final ActivityTestRule<SplashActivity> splashActivityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    private Context appContext;

    @Before
    public void setup() {
        // AUT context
        appContext = InstrumentationRegistry.getTargetContext();

        // Access the app objects to reset data
        // Different files are used depending on current build variant
        if (AccountPersistenceService.accountsFile(appContext).exists()) {
            deleteAccounts();
        }
    }

    @After
    public void teardown() {
        // Access the app objects to reset data
        // Different files are used depending on current build variant
        deleteAccounts();
        UserSession.getInstance().logout();
    }

    private void deleteAccounts() {
        //noinspection ResultOfMethodCallIgnored
        AccountPersistenceService.accountsFile(appContext).delete();
        AccountsApi.getInstance().removeAllAccounts();
    }

    @Test
    public void whenUserLinksAccountItAppearsInOverview() {
        // prepare application state
        injectFakeAccounts(TEST_ACCOUNT);

        // perform actions in the UI
        login(VALID_EMAIL, VALID_PASSWORD);
        openManageAccountsScreen();
        linkAccount(BANK_NAME, ACCOUNT_NUMBER, VALID_PASSWORD);
        goBackToOverviewScreen();
        verifyAccountOverviewIsShown(DISPLAY_NAME, ACCOUNT_NUMBER_LAST_DIGITS, BALANCE, AMOUNT_SPENT_THIS_MONTH);
        verifyTransactionsAreShown(
                new ExpectedTransaction(TRANSACTION_1_TITLE, TRANSACTION_1_AMOUNT_AS_STRING),
                new ExpectedTransaction(TRANSACTION_2_TITLE, TRANSACTION_2_AMOUNT_AS_STRING)
        );
    }

    private void injectFakeAccounts(BankAccount... accounts) {
        BankAccountsDatasource.getInstance().injectAccounts(accounts);
    }

    private void login(String email, String password) {
        onView(withId(R.id.edittext_email))
                .perform(typeText(email));
        onView(withId(R.id.edittext_password))
                .perform(typeText(password));
        onView(withId(R.id.button_sign_in))
                .perform(click());
    }

    private void openManageAccountsScreen() {
        onView(withId(R.id.fab_manage_accounts))
                .perform(click());
    }

    private void linkAccount(String bankName,
                             String accountNumber,
                             String validPassword) {
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
    }

    private void goBackToOverviewScreen() {
        pressBack();
    }

    private void verifyAccountOverviewIsShown(String displayName,
                                              String accountNumberLastDigits,
                                              String balance,
                                              String amountSpentThisMonth) {
        onView(withId(R.id.recyclerview_accounts))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(accountNumberLastDigits))))
                .check(matches(allOf(
                        hasDescendant(allOf(
                                withId(R.id.textview_account_last_digits),
                                withText(accountNumberLastDigits))),
                        hasDescendant(allOf(
                                withId(R.id.textview_display_name),
                                withText(displayName))),
                        hasDescendant(allOf(
                                withId(R.id.textview_balance),
                                withText(balance))),
                        hasDescendant(allOf(
                                withId(R.id.textview_amount_spent_this_month),
                                withText(amountSpentThisMonth))))));
    }

    private void verifyTransactionIsShown(ExpectedTransaction transaction) {
        onData(hasDescriptionAndAmount(transaction.description, transaction.amount))
                .inAdapterView(withId(R.id.listview_transactions))
                .onChildView(withId(R.id.textview_transaction_description))
                .check(matches(isDisplayed()));
    }

    private void verifyTransactionsAreShown(ExpectedTransaction... transactions) {
        for (ExpectedTransaction transaction : transactions) {
            verifyTransactionIsShown(transaction);
        }
    }

    private Matcher<? super Object> hasDescriptionAndAmount(String description, String amount) {
        return new TransactionListItemViewModelMatcher(description, amount);
    }

    private static class ExpectedTransaction {

        final String description;
        final String amount;

        ExpectedTransaction(String description, String amount) {
            this.description = description;
            this.amount = amount;
        }
    }

    private static class TransactionListItemViewModelMatcher extends BoundedMatcher<Object, TransactionListItemViewModel> {
        private final String description;
        private final String amount;

        public TransactionListItemViewModelMatcher(String description, String amount) {
            super(TransactionListItemViewModel.class);
            this.description = description;
            this.amount = amount;
        }

        @Override
        protected boolean matchesSafely(TransactionListItemViewModel item) {
            return description.equals(item.description()) && amount.equals(item.amount());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("expected description '" + this.description + "'" +
                    " and amount '" + this.amount + "'");
        }
    }
}
