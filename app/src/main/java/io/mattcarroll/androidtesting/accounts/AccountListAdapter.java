package io.mattcarroll.androidtesting.accounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

class AccountListAdapter extends RecyclerView.Adapter<AccountListItemViewHolder> {
    private final Context context;
    private List<AccountListItemViewModel> viewModels = Collections.emptyList();

    public AccountListAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setViewModels(@NonNull List<AccountListItemViewModel> viewModels) {
        this.viewModels = viewModels;
        notifyDataSetChanged();
    }

    @Override
    public AccountListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountListItemView accountView = new AccountListItemView(context);
        return new AccountListItemViewHolder(accountView);
    }

    @Override
    public void onBindViewHolder(AccountListItemViewHolder holder, int position) {
        holder.setViewModel(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }
}
