package io.mattcarroll.androidtesting.overview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import io.mattcarroll.androidtesting.R;

public class AccountsAdapter extends RecyclerView.Adapter<AccountViewHolder> {

    private final Context context;
    private List<AccountViewModel> viewModels = Collections.emptyList();

    public AccountsAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setViewModels(@NonNull List<AccountViewModel> viewModels) {
        this.viewModels = viewModels;
        notifyDataSetChanged();
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountView accountView = new AccountView(context);
        return new AccountViewHolder(accountView);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.setViewModel(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

}
