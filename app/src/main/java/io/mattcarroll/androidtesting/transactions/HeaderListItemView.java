package io.mattcarroll.androidtesting.transactions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

/**
 * View that renders the header for a Transaction category.
 */
public class HeaderListItemView extends FrameLayout {

    private TextView titleTextView;
    private TextView subtitleTextView;

    public HeaderListItemView(Context context) {
        this(context, null);
    }

    public HeaderListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_transaction_header_list_item, this, true);
        titleTextView = (TextView) findViewById(R.id.textview_title);
        subtitleTextView = (TextView) findViewById(R.id.textview_subtitle);
    }

    public void setViewModel(@Nullable HeaderListItemViewModel viewModel) {
        if (null != viewModel) {
            titleTextView.setText(viewModel.getTitle());

            subtitleTextView.setText(viewModel.getSubtitle());
            subtitleTextView.setVisibility(!TextUtils.isEmpty(viewModel.getSubtitle()) ? VISIBLE : GONE);
        } else {
            titleTextView.setText(null);
            subtitleTextView.setText(null);
        }
    }
}
