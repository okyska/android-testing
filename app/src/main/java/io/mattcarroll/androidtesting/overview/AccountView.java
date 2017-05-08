package io.mattcarroll.androidtesting.overview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

/**
 * Provides a card-like view that shows info about an account,
 * such as the account name and balance.
 */
class AccountView extends FrameLayout {
    private TextView displayNameView;
    private TextView accountLastDigitsView;
    private TextView balanceView;
    private TextView amountSpentThisMonthView;

    public AccountView(Context context) {
        this(context, null);
    }

    public AccountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_account_view, this, true);

        displayNameView = (TextView) findViewById(R.id.textview_display_name);
        accountLastDigitsView = (TextView) findViewById(R.id.textview_account_last_digits);
        balanceView = (TextView) findViewById(R.id.textview_balance);
        amountSpentThisMonthView = (TextView) findViewById(R.id.textview_amount_spent_this_month);
    }

    public void setViewModel(@NonNull AccountViewModel viewModel) {
        displayNameView.setText(viewModel.getDisplayName());
        accountLastDigitsView.setText(viewModel.getAccountLastDigits());
        balanceView.setText(viewModel.getBalance());
        amountSpentThisMonthView.setText(viewModel.getAmountSpentThisMonth());
    }

}
