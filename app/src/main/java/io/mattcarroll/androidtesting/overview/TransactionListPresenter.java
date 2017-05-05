package io.mattcarroll.androidtesting.overview;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.accounts.Transaction;

/**
 * Presenter that shows a list of Transactions
 */
class TransactionListPresenter {
    private static final SimpleDateFormat FULL_DATE_FORMAT =
            new SimpleDateFormat("MMM d, yyyy", Locale.US); // e.g. Aug 1, 2017

    private static final SimpleDateFormat THIS_WEEK_DATE_FORMAT =
            new SimpleDateFormat("EEEE", Locale.US); // e.g. Thursday

    private final Resources resources;
    private final NumberFormat currencyFormat;

    public TransactionListPresenter(@NonNull Resources resources, @NonNull NumberFormat currencyFormat) {
        this.resources = resources;
        this.currencyFormat = currencyFormat;
    }

    @NonNull
    public List<Object> present(@NonNull List<Transaction> transactions) {
        if (transactions.size() == 0) {
            return Collections.emptyList();
        }

        List<Transaction> transactionsSortedByDate = sortByDateMostRecentFirst(transactions);

        List<Object> viewModels = new ArrayList<>();

        long currentDate = 0;

        for (Transaction transaction : transactionsSortedByDate) {
            if (!isOnSameDay(transaction.date(), currentDate)) {
                currentDate = transaction.date();
                viewModels.add(presentDateHeader(currentDate));
            }
            viewModels.add(presentTransaction(transaction));
        }

        return viewModels;
    }

    @NonNull
    private List<Transaction> sortByDateMostRecentFirst(@NonNull List<Transaction> transactions) {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        Collections.sort(sortedTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if (o1.date() > o2.date()) {
                    return -1;
                } else if (o1.date() < o2.date()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return sortedTransactions;
    }

    private boolean isOnSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date(date1));
        cal2.setTime(new Date(date2));
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    @NonNull
    private HeaderListItemViewModel presentDateHeader(long date) {
        final String dateAsString;
        if (DateUtils.isToday(date)) {
            dateAsString = resources.getString(R.string.label_today);
        } else if (isYesterday(date)) {
            dateAsString = resources.getString(R.string.label_yesterday);
        } else if (isThisWeek(date)) {
            dateAsString = THIS_WEEK_DATE_FORMAT.format(new Date(date));
        } else {
            dateAsString = FULL_DATE_FORMAT.format(new Date(date));
        }

        return new HeaderListItemViewModel(dateAsString);
    }

    private boolean isYesterday(long date) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(date));

        return isSameYear(c1, c2) && isSameDayOfYear(c1, c2);
    }

    private boolean isSameYear(@NonNull Calendar c1, @NonNull Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    private boolean isSameDayOfYear(@NonNull Calendar c1, @NonNull Calendar c2) {
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isThisWeek(long date) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(date));

        return isSameYear(c1, c2) && isSameWeekOfYear(c1, c2);
    }

    private boolean isSameWeekOfYear(@NonNull Calendar c1, @NonNull Calendar c2) {
        return c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
    }

    @NonNull
    private TransactionListItemViewModel presentTransaction(@NonNull Transaction transaction) {
        String title = transaction.description();
        String amount = presentTransactionAmount(transaction.amountInCents());
        return new TransactionListItemViewModel(R.drawable.ic_receipt, title, amount);
    }

    @NonNull
    private String presentTransactionAmount(long amountInCents) {
        BigDecimal amountInDollars = new BigDecimal(amountInCents).movePointLeft(2);
        return currencyFormat.format(amountInDollars);
    }
}
