package io.mattcarroll.androidtesting.creditcardanalysis;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import io.mattcarroll.androidtesting.R;

/**
 * View that displays a total balance and annualized interest across all of
 * the user's credit card.
 */
public class CreditCardBalancesView extends FrameLayout {

    private TextView totalBalanceAmountTextView;
    private TextView annualizedInterestView;

    public CreditCardBalancesView(Context context) {
        this(context, null);
    }

    public CreditCardBalancesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_credit_card_balances, this, true);
        totalBalanceAmountTextView = (TextView) findViewById(R.id.textview_total_balance_amount);
        annualizedInterestView = (TextView) findViewById(R.id.textview_annualized_interest_amount);
    }

    public void setViewModel(@Nullable CreditCardBalanceViewModel viewModel) {
        if (null != viewModel) {
            String totalBalanceMoney = formatMoneyFromCents(viewModel.getTotalBalanceInCents());
            totalBalanceAmountTextView.setText("(" + totalBalanceMoney + ")");

            if (null != viewModel.getAnnualizedInterestInCents()) {
                String annualizedMoney = formatMoneyFromCents(viewModel.getAnnualizedInterestInCents());
                annualizedInterestView.setText(annualizedMoney);
            } else {
                annualizedInterestView.setText("");
            }
        } else {
            totalBalanceAmountTextView.setText("");
            annualizedInterestView.setText("");
        }
    }

    private String formatMoneyFromCents(int cents) {
        return String.format(Locale.US, "$%,d", Math.round((float) cents / 100));
    }
}
