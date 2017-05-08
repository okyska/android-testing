package io.mattcarroll.androidtesting.overview;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.accounts.Transaction;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionListPresenterTest {
    private static final int MAY = 4; // month number is 0-based
    private static final Calendar MAY_9_2017 = new GregorianCalendar(2017, MAY, 9);

    private static final long TODAY = MAY_9_2017.getTimeInMillis();
    private static final long YESTERDAY = daysAgo(1);
    private static final long FOUR_DAYS_AGO = daysAgo(4);
    private static final long TWELVE_DAYS_AGO = daysAgo(12);

    private static final String TODAY_STRING = "Today";
    private static final String YESTERDAY_STRING = "Yesterday";
    private static final String FOUR_DAYS_AGO_STRING = "Friday";
    private static final String TWELVE_DAYS_AGO_STRING = "Apr 27, 2017";
    private static final String DESCRIPTION = "Some description";
    private static final int ONE_DOLLAR_IN_CENTS = 100;
    private static final String ONE_DOLLAR_AS_STRING = "$1.00";

    private static long daysAgo(int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(TODAY));
        calendar.add(Calendar.DATE, -1 * days);
        return calendar.getTimeInMillis();
    }

    private TransactionListPresenter presenter;

    @Mock
    private Resources mockResources;
    @Mock
    private Time mockCalendar;

    @Before
    public void setup() {
        presenter = new TransactionListPresenter(
                mockResources,
                mockCalendar,
                NumberFormat.getCurrencyInstance(Locale.US));

        when(mockCalendar.isToday(TODAY)).thenReturn(true);
        when(mockCalendar.isYesterday(YESTERDAY)).thenReturn(true);
        when(mockCalendar.isThisWeek(FOUR_DAYS_AGO)).thenReturn(true);

        when(mockResources.getString(R.string.label_today)).thenReturn(TODAY_STRING);
        when(mockResources.getString(R.string.label_yesterday)).thenReturn(YESTERDAY_STRING);
    }

    @Test
    public void itPresentsViewModelsSortedByDateMostRecentFirst() {
        List<Transaction> transactions = transactionsWithDates(
                TWELVE_DAYS_AGO, TODAY, FOUR_DAYS_AGO, YESTERDAY);

        List<Object> viewModels = presenter.present(transactions);

        HeaderListItemViewModel header1 = (HeaderListItemViewModel) viewModels.get(0);
        HeaderListItemViewModel header2 = (HeaderListItemViewModel) viewModels.get(2);
        HeaderListItemViewModel header3 = (HeaderListItemViewModel) viewModels.get(4);
        HeaderListItemViewModel header4 = (HeaderListItemViewModel) viewModels.get(6);

        assertEquals(TODAY_STRING, header1.transactionDate());
        assertEquals(YESTERDAY_STRING, header2.transactionDate());
        assertEquals(FOUR_DAYS_AGO_STRING, header3.transactionDate());
        assertEquals(TWELVE_DAYS_AGO_STRING, header4.transactionDate());
    }

    @Test
    public void itPresentsTodayHeaderGivenTransactionFromToday() {
        List<Transaction> transactions = transactionsWithDates(TODAY);

        List<Object> viewModels = presenter.present(transactions);

        HeaderListItemViewModel viewModel = (HeaderListItemViewModel) viewModels.get(0);
        assertEquals(TODAY_STRING, viewModel.transactionDate());
    }

    @Test
    public void itPresentsYesterdayHeaderGivenTransactionFromYesterday() {
        List<Transaction> transactions = transactionsWithDates(YESTERDAY);

        List<Object> viewModels = presenter.present(transactions);

        HeaderListItemViewModel viewModel = (HeaderListItemViewModel) viewModels.get(0);
        assertEquals(YESTERDAY_STRING, viewModel.transactionDate());
    }

    @Test
    public void itPresentsDayOfTheWeekHeaderGivenTransactionFromThisWeek() {
        List<Transaction> transactions = transactionsWithDates(FOUR_DAYS_AGO);

        List<Object> viewModels = presenter.present(transactions);

        HeaderListItemViewModel viewModel = (HeaderListItemViewModel) viewModels.get(0);
        assertEquals(FOUR_DAYS_AGO_STRING, viewModel.transactionDate());
    }

    @Test
    public void itPresentsFullDateHeaderGivenTransactionFromMoreThanAWeekAgo() {
        List<Transaction> transactions = transactionsWithDates(TWELVE_DAYS_AGO);

        List<Object> viewModels = presenter.present(transactions);

        HeaderListItemViewModel viewModel = (HeaderListItemViewModel) viewModels.get(0);
        assertEquals(TWELVE_DAYS_AGO_STRING, viewModel.transactionDate());
    }

    @Test
    public void itPresentsTransactionDescriptionAsTitle() {
        List<Transaction> transactions = transactionsWithDescriptions(DESCRIPTION);

        List<Object> viewModels = presenter.present(transactions);

        TransactionListItemViewModel viewModel = (TransactionListItemViewModel) viewModels.get(1);
        assertEquals(DESCRIPTION, viewModel.description());
    }

    @Test
    public void itPresentsTransactionAmountAsDetail() {
        List<Transaction> transactions = transactionsWithAmountsInCents(ONE_DOLLAR_IN_CENTS);

        List<Object> viewModels = presenter.present(transactions);

        TransactionListItemViewModel viewModel = (TransactionListItemViewModel) viewModels.get(1);
        assertEquals(ONE_DOLLAR_AS_STRING, viewModel.amount());
    }

    @NonNull
    private List<Transaction> transactionsWithDates(@NonNull Long... dates) {
        List<Transaction> transactions = new ArrayList<>(dates.length);
        for (Long date : dates) {
            transactions.add(new Transaction("", 1, date));
        }
        return transactions;
    }

    @NonNull
    private List<Transaction> transactionsWithDescriptions(@NonNull String... descriptions) {
        List<Transaction> transactions = new ArrayList<>(descriptions.length);
        for (String description : descriptions) {
            transactions.add(new Transaction(description, 1, TODAY));
        }
        return transactions;
    }

    @NonNull
    private List<Transaction> transactionsWithAmountsInCents(@NonNull Integer... amounts) {
        List<Transaction> transactions = new ArrayList<>(amounts.length);
        for (Integer amountInCents : amounts) {
            transactions.add(new Transaction("", amountInCents, TODAY));
        }
        return transactions;
    }
}
