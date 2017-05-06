package io.mattcarroll.androidtesting.overview;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.mattcarroll.androidtesting.Bus;
import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.accounts.AccountsApi;
import io.mattcarroll.androidtesting.accounts.AccountsService;
import io.mattcarroll.androidtesting.accounts.BankAccountsChangedEvent;
import io.mattcarroll.androidtesting.accounts.Transaction;

/**
 * Display an overview of the user's accounts.
 */
public class AccountsOverviewFragment extends Fragment {

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            api = ((AccountsService.LocalBinder) service).getApi();
            updatePresentation();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            api = null;
        }
    };

    private AccountsApi api;

    @NonNull
    public static AccountsOverviewFragment newInstance() {
        return new AccountsOverviewFragment();
    }

    private AccountsPresenter accountsPresenter;
    private List<AccountViewModel> accountViewModels;
    private AccountsAdapter accountsAdapter;
    private RecyclerView accountsView;
    private String visibleAccountId = null;

    private TransactionListPresenter transactionListPresenter;
    private TransactionListAdapter transactionListAdapter;
    private ListView transactionsView;

    public AccountsOverviewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bindAccountsService();
    }

    private void bindAccountsService() {
        Intent accountsServiceIntent = new Intent(getActivity(), AccountsService.class);
        getActivity().bindService(accountsServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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

    @Override
    public void onStart() {
        super.onStart();
        Bus.getBus().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePresentation();
    }

    @Override
    public void onStop() {
        Bus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        getActivity().unbindService(serviceConnection);
        super.onDetach();
    }

    public void onEventMainThread(@NonNull BankAccountsChangedEvent event) {
        updatePresentation();
    }

    private void initAccountsRecyclerView(@NonNull View view) {
        accountsView = (RecyclerView) view.findViewById(R.id.recyclerview_accounts);
        accountsAdapter = new AccountsAdapter(getContext());

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
    }

    private void showTransactionsForAccount(@NonNull String accountId) {
        if (api == null) {
            return;
        }
        List<Transaction> transactions = api.listTransactions(accountId);
        transactionListPresenter = new TransactionListPresenter(getResources(), NumberFormat.getCurrencyInstance());
        transactionListAdapter.setViewModels(transactionListPresenter.present(transactions));
    }

    private void updatePresentation() {
        if (api == null || getView() == null) {
            return;
        }

        accountsPresenter = new AccountsPresenter(NumberFormat.getCurrencyInstance());
        accountViewModels = accountsPresenter.present(api.accounts());
        accountsAdapter.setViewModels(accountViewModels);

        if (visibleAccountId == null && accountViewModels.size() > 0) {
            visibleAccountId = accountViewModels.get(0).getAccountId();
        }

        if (visibleAccountId != null) {
            showTransactionsForAccount(visibleAccountId);
        }

        int noAccountsViewVisibility = accountViewModels.size() == 0 ? View.VISIBLE : View.GONE;
        getView().findViewById(R.id.textview_no_accounts).setVisibility(noAccountsViewVisibility);
    }

}
