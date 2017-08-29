package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ListAdapter} that presents financial transaction.
 */
class TransactionListAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_TRANSACTION = 1;

    private List<Object> viewModels = new ArrayList<>();

    public void setViewModels(@NonNull List<Object> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return viewModels.size();
    }

    @Override
    public Object getItem(int position) {
        return viewModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof HeaderListItemViewModel) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_TRANSACTION;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Object viewModel = getItem(position);

        if (VIEW_TYPE_HEADER == getItemViewType(position)) {
            if (null == view) {
                view = new HeaderListItemView(viewGroup.getContext());
            }
            ((HeaderListItemView) view).setViewModel((HeaderListItemViewModel) viewModel);
        } else {
            if (null == view) {
                view = new TransactionListItemView(viewGroup.getContext());
            }
            ((TransactionListItemView) view).setViewModel((TransactionListItemViewModel) viewModel);
        }

        return view;
    }
}
