package io.mattcarroll.androidtesting.accounts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Set;

import io.mattcarroll.androidtesting.R;

public class ManageAccountsActivity extends AppCompatActivity {
    private RecyclerView accountsView;
    private AccountListAdapter accountListAdapter;
    private AccountListPresenter accountListPresenter;
    private List<AccountListItemViewModel> accountListViewModels;
    private Set<BankAccount> accounts;
    private AccountsApi api;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            api = ((AccountsService.LocalBinder) service).getApi();
            loadAccounts();
            updatePresentation();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            api = null;
        }
    };
    private Button linkAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        accountsView = (RecyclerView) findViewById(R.id.recyclerview_accounts_list);

        initAccountsView(accountsView);
        bindAccountsService();

        linkAccountButton = (Button) findViewById(R.id.button_link_account);
        linkAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO launch link account screen
            }
        });
    }

    private void initAccountsView(@NonNull RecyclerView accountsView) {
        accountListAdapter = new AccountListAdapter(this);
        AccountNumberMask mask = AccountNumberMaskFactory.provideLastFourDigitsMask();
        accountListPresenter = new AccountListPresenter(mask,
                new AccountListItemView.OnRemoveClickListener() {
                    @Override
                    public void onRemoveClick(@NonNull String accountId) {
                        removeAccount(accountId);
                        updatePresentation();
                    }
                });

        final LinearLayoutManager accountsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        accountsView.addItemDecoration(dividerItemDecoration);
        accountsView.setLayoutManager(accountsLayoutManager);
        accountsView.setAdapter(accountListAdapter);
    }

    private void bindAccountsService() {
        Intent accountsServiceIntent = new Intent(this, AccountsService.class);
        bindService(accountsServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void loadAccounts() {
        if (api != null) {
            accounts = api.accounts();
        }
    }

    private void removeAccount(@NonNull String accountId) {
        if (api != null) {
            api.removeAccount(accountId);
        }

        BankAccount accountToRemove = null;
        for (BankAccount account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                accountToRemove = account;
                break;
            }
        }

        accounts.remove(accountToRemove);
        updatePresentation();
    }

    private void updatePresentation() {
        accountListViewModels = accountListPresenter.present(accounts);
        accountListAdapter.setViewModels(accountListViewModels);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
