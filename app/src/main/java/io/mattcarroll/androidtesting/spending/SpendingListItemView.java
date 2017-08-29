package io.mattcarroll.androidtesting.spending;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.mattcarroll.androidtesting.R;

public class SpendingListItemView extends FrameLayout {

    private ImageView categoryIconView;
    private TextView categoryNameView;
    private TextView totalSpentView;

    public SpendingListItemView(@NonNull Context context) {
        this(context, null);
    }

    public SpendingListItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.spending_list_item, this, true);

        categoryIconView = (ImageView) findViewById(R.id.imageview_category_icon);
        categoryNameView = (TextView) findViewById(R.id.textview_category_name);
        totalSpentView = (TextView) findViewById(R.id.textview_total_spent);
    }

    public void setViewModel(@NonNull SpendingListItemViewModel viewModel) {
        categoryIconView.setImageDrawable(viewModel.getCategoryIcon());
        categoryNameView.setText(viewModel.getCategoryName());
        totalSpentView.setText(viewModel.getTotalSpent());
    }

}
