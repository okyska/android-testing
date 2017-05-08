package io.mattcarroll.androidtesting.overview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

/**
 * View that renders a Transaction as a list item.
 */
class TransactionListItemView extends FrameLayout {

    private ImageView mIconImageView;
    private TextView mTitleTextView;
    private TextView mSubtitleTextView;
    private TextView mDetailTextView;

    public TransactionListItemView(Context context) {
        this(context, null);
    }

    public TransactionListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_transaction_list_item, this, true);
        mIconImageView = (ImageView) findViewById(R.id.imageview_icon);
        mTitleTextView = (TextView) findViewById(R.id.textview_transaction_title);
        mSubtitleTextView = (TextView) findViewById(R.id.textview_subtitle);
        mDetailTextView = (TextView) findViewById(R.id.textview_detail);
    }

    public void setViewModel(@Nullable TransactionListItemViewModel viewModel) {
        if (null != viewModel) {
            if (null != viewModel.icon()) {
                mIconImageView.setImageResource(viewModel.icon());
                mIconImageView.setVisibility(VISIBLE);
            } else {
                mIconImageView.setImageDrawable(null);
                mIconImageView.setVisibility(GONE);
            }

            mTitleTextView.setText(viewModel.title());

            mSubtitleTextView.setText(viewModel.subtitle());
            mSubtitleTextView.setVisibility(!TextUtils.isEmpty(viewModel.subtitle()) ? VISIBLE : GONE);

            mDetailTextView.setText(viewModel.detail());
            mDetailTextView.setVisibility(!TextUtils.isEmpty(viewModel.detail()) ? VISIBLE : GONE);
        } else {
            mIconImageView.setImageDrawable(null);
            mTitleTextView.setText(null);
            mSubtitleTextView.setText(null);
            mDetailTextView.setText(null);
        }
    }
}
