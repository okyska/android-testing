package io.mattcarroll.androidtesting.overview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

/**
 * View that renders the header for a Transaction category.
 */
class HeaderListItemView extends FrameLayout {

    private TextView transactionDateTextView;

    public HeaderListItemView(Context context) {
        this(context, null);
    }

    public HeaderListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_transaction_header_list_item, this, true);
        transactionDateTextView = (TextView) findViewById(R.id.textview_transaction_date);
    }

    public void setViewModel(@Nullable HeaderListItemViewModel viewModel) {
        if (null != viewModel) {
            transactionDateTextView.setText(viewModel.transactionDate());
        } else {
            transactionDateTextView.setText(null);
        }
    }
}
