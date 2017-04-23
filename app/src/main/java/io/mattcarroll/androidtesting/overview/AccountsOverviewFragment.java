package io.mattcarroll.androidtesting.overview;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.mattcarroll.androidtesting.R;

/**
 * Display an overview of the user's accounts.
 */
public class AccountsOverviewFragment extends Fragment {

    private static final Set<Account> DUMMY_ACCOUNTS = new HashSet<>(Arrays.asList(
            new Account(
                    "Chase Sapphire Reserve",
                    "0000111122223333",
                    new BigDecimal(350),
                    new BigDecimal(1234)
            ),
            new Account(
                    "Discover Mega Cash Back Extreme",
                    "9999888877776666",
                    new BigDecimal(678),
                    new BigDecimal(884)
            ),
            new Account(
                    "Chipotle GuacBack™ Rewards",
                    "01101111000101011100",
                    new BigDecimal(1240),
                    new BigDecimal(7844)
            ),
            new Account(
                    "Chase United Ejector Card",
                    "5555012355550987",
                    new BigDecimal(0),
                    new BigDecimal(0)
            )
    ));

    @NonNull
    public static AccountsOverviewFragment newInstance() {
        return new AccountsOverviewFragment();
    }

    private AccountsPresenter presenter;
    private AccountsAdapter adapter;
    private RecyclerView accountsView;

    public AccountsOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        accountsView = (RecyclerView) view.findViewById(R.id.recyclerview_accounts);

        presenter = new AccountsPresenter(NumberFormat.getCurrencyInstance());
        adapter = new AccountsAdapter(getContext());
        List<AccountViewModel> viewModels = presenter.present(DUMMY_ACCOUNTS);
        adapter.setViewModels(viewModels);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        accountsView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(accountsView);
        accountsView.setAdapter(adapter);
    }

}
