package io.mattcarroll.androidtesting.transactions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.mattcarroll.androidtesting.R;

/**
 * Lists transactions.
 */
public class TransactionListFragment extends Fragment {

    @NonNull
    public static TransactionListFragment newInstance() {
        return new TransactionListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransactionListPresenter listPresenter = new TransactionListPresenter();

        TransactionListAdapter adapter = new TransactionListAdapter();
        adapter.setViewModels(listPresenter.getTransactionListItems());

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
