package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

/**
 * Renders summary information about an Account for display
 * in a list of Accounts.
 */
class AccountListItemView extends RelativeLayout {

    private TextView accountNameView;
    private TextView accountNumberView;
    private OnRemoveClickListener listener;
    private String accountId;

    public AccountListItemView(Context context) {
        this(context, null);
    }

    public AccountListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_account_list_item, this, true);
        accountNameView = (TextView) findViewById(R.id.textview_account_name);
        accountNumberView = (TextView) findViewById(R.id.textview_account_number);
        Button removeButton = (Button) findViewById(R.id.button_remove_account);

        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRemoveClick(accountId);
                }
            }
        });
    }

    public void setViewModel(@NonNull AccountListItemViewModel viewModel) {
        accountId = viewModel.getAccountId();
        accountNameView.setText(viewModel.getAccountName());
        accountNumberView.setText(viewModel.getMaskedAccountNumber());
        this.listener = viewModel.getOnRemoveClickListener();
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(@NonNull String accountId);
    }
}
