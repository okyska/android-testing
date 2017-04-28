package io.mattcarroll.androidtesting.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mattcarroll.androidtesting.R;
import io.mattcarroll.androidtesting.overview.Account;

public class ManageAccountsActivity extends AppCompatActivity {
    private static final Map<String, Account> DUMMY_ACCOUNTS = new HashMap<>();
    static {
        final Account account1 = new Account(
                "account1",
                "Chase Sapphire Reserve",
                "0000111122223333",
                new BigDecimal(350),
                new BigDecimal(1234)
        );

        final Account account2 = new Account(
                "account2",
                "Discover Mega Cash Back Extreme",
                "9999888877776666",
                new BigDecimal(678),
                new BigDecimal(884)
        );

        final Account account3 = new Account(
                "account3",
                "Chipotle GuacBack™ Rewards",
                "01101111000101011100",
                new BigDecimal(1240),
                new BigDecimal(7844)
        );

        final Account account4 = new Account(
                "account4",
                "Chase United Ejector Card",
                "5555012355550987",
                new BigDecimal(0),
                new BigDecimal(0)
        );

        DUMMY_ACCOUNTS.put(account1.getAccountId(), account1);
        DUMMY_ACCOUNTS.put(account2.getAccountId(), account2);
        DUMMY_ACCOUNTS.put(account3.getAccountId(), account3);
        DUMMY_ACCOUNTS.put(account4.getAccountId(), account4);
    }

    private RecyclerView accountsView;
    private AccountListAdapter accountListAdapter;
    private AccountListPresenter accountListPresenter;
    private List<AccountListItemViewModel> accountListViewModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        accountsView = (RecyclerView) findViewById(R.id.recyclerview_accounts_list);

        accountListAdapter = new AccountListAdapter(this);
        AccountNumberMask mask = AccountNumberMaskFactory.provideLastFourDigitsMask();
        accountListPresenter = new AccountListPresenter(mask,
                new AccountListItemView.OnRemoveClickListener() {
                    @Override
                    public void onRemoveClick(@NonNull String accountId) {
                        DUMMY_ACCOUNTS.remove(accountId);
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

        updatePresentation();
    }

    private void updatePresentation() {
        accountListViewModels = accountListPresenter.present(DUMMY_ACCOUNTS.values());
        accountListAdapter.setViewModels(accountListViewModels);
    }
}
