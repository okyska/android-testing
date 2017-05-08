package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

class AccountListItemViewHolder extends RecyclerView.ViewHolder {

    private final AccountListItemView listItemView;

    public AccountListItemViewHolder(@NonNull AccountListItemView listItemView) {
        super(listItemView);
        this.listItemView = listItemView;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listItemView.setLayoutParams(params);
    }

    public void setViewModel(@NonNull AccountListItemViewModel viewModel) {
        listItemView.setViewModel(viewModel);
    }
}
