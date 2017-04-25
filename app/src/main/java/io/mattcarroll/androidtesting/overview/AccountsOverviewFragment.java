package io.mattcarroll.androidtesting.overview;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private static final String TAG = "AccountsOverviewFragment";

    private static final Set<Account> DUMMY_ACCOUNTS = new HashSet<>(Arrays.asList(
            new Account(
                    "account1",
                    "Chase Sapphire Reserve",
                    "0000111122223333",
                    new BigDecimal(350),
                    new BigDecimal(1234)
            ),
            new Account(
                    "account2",
                    "Discover Mega Cash Back Extreme",
                    "9999888877776666",
                    new BigDecimal(678),
                    new BigDecimal(884)
            ),
            new Account(
                    "account3",
                    "Chipotle GuacBack™ Rewards",
                    "01101111000101011100",
                    new BigDecimal(1240),
                    new BigDecimal(7844)
            ),
            new Account(
                    "account4",
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

    private AccountsPresenter accountsPresenter;
    private List<AccountViewModel> accountViewModels;
    private AccountsAdapter accountsAdapter;
    private RecyclerView accountsView;
    private String visibleAccountId;

    TransactionListPresenter transactionListPresenter;
    private TransactionListAdapter transactionListAdapter;
    private ListView transactionsView;

    public AccountsOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initAccountsRecyclerView(view);
        initTransactionsListView(view);
    }

    private void initAccountsRecyclerView(@NonNull View view) {
        accountsView = (RecyclerView) view.findViewById(R.id.recyclerview_accounts);

        accountsPresenter = new AccountsPresenter(NumberFormat.getCurrencyInstance());
        accountsAdapter = new AccountsAdapter(getContext());
        accountViewModels = accountsPresenter.present(DUMMY_ACCOUNTS);
        accountsAdapter.setViewModels(accountViewModels);
        visibleAccountId = accountViewModels.get(0).getAccountId();

        final LinearLayoutManager accountsLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        accountsView.setLayoutManager(accountsLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(accountsView);
        accountsView.setAdapter(accountsAdapter);
        accountsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = accountsLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position < 0) return;

                    showTransactionsForAccountAtPosition(position);
                }
            }
        });
    }

    private void showTransactionsForAccountAtPosition(int position) {
        AccountViewModel viewModel = accountViewModels.get(position);
        String accountId = viewModel.getAccountId();
        if (!accountId.equals(visibleAccountId)) {
            showTransactionsForAccount(accountId);
            visibleAccountId = accountId;
        }
    }

    private void initTransactionsListView(@NonNull View view) {
        transactionsView = (ListView) view.findViewById(R.id.listview_transactions);

        transactionListAdapter = new TransactionListAdapter();
        transactionsView.setAdapter(transactionListAdapter);
        showTransactionsForAccount(visibleAccountId);
    }

    private void showTransactionsForAccount(@NonNull String accountId) {
        // TODO obtain transactions for the given account and present them
        Log.d(TAG, "Presenting transactions for account: " + accountId);
        transactionListPresenter = new TransactionListPresenter();
        transactionListAdapter.setViewModels(transactionListPresenter.getTransactionListItems());
    }

}
