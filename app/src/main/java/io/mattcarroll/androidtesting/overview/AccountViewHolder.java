package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.R;

class AccountViewHolder extends RecyclerView.ViewHolder {

    private final AccountView accountView;

    public AccountViewHolder(@NonNull AccountView accountView) {
        super(accountView);
        this.accountView = accountView;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        @Px int margin = accountView.getResources().getDimensionPixelSize(R.dimen.account_view_margin);
        params.setMargins(margin, margin, margin, margin);
        accountView.setLayoutParams(params);
    }

    public void setViewModel(@NonNull AccountViewModel viewModel) {
        accountView.setViewModel(viewModel);
    }
}
