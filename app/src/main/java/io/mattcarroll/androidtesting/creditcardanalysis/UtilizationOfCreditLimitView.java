package io.mattcarroll.androidtesting.creditcardanalysis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import io.mattcarroll.androidtesting.R;

/**
 * View that displays credit used vs credit available across all of a user's
 * credit cards.
 */
public class UtilizationOfCreditLimitView extends FrameLayout {

    private TextView usedVsAvailableTextView;
    private TextView percentageTextView;
    private MeterView meterView;

    public UtilizationOfCreditLimitView(Context context) {
        this(context, null);
    }

    public UtilizationOfCreditLimitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_utilization_of_credit_limit, this, true);
        usedVsAvailableTextView = (TextView) findViewById(R.id.textview_used_vs_available);
        percentageTextView = (TextView) findViewById(R.id.textview_percentage);
        meterView = (MeterView) findViewById(R.id.meterview);
    }

    public void setViewModel(@Nullable UtilizationOfCreditLimitViewModel viewModel) {
        if (null != viewModel) {
            usedVsAvailableTextView.setText(formatUsedVsAvailable(viewModel));
            percentageTextView.setText(formatPercentage(viewModel));
            meterView.setMeterPercentage(getUsedPercent(viewModel));
        } else {
            usedVsAvailableTextView.setText("");
            percentageTextView.setText("");
            meterView.setMeterPercentage(0);
        }
    }

    @NonNull
    private String formatUsedVsAvailable(@NonNull UtilizationOfCreditLimitViewModel viewModel) {
        return formatMoneyFromCents(viewModel.getCreditUsedInCents()) + " / "
                + formatMoneyFromCents(viewModel.getCreditAvailableInCents());
    }

    private String formatMoneyFromCents(int cents) {
        return String.format(Locale.US, "$%,d", Math.round((float) cents / 100));
    }

    @NonNull
    private String formatPercentage(@NonNull UtilizationOfCreditLimitViewModel viewModel) {
        return String.format(Locale.US, "%d%%", Math.round(getUsedPercent(viewModel) * 100));
    }

    private float getUsedPercent(@NonNull UtilizationOfCreditLimitViewModel viewModel) {
        return (float) viewModel.getCreditUsedInCents() / viewModel.getCreditAvailableInCents();
    }
}
