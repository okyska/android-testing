package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.mattcarroll.androidtesting.R;

/**
 * Presenter that shows a list of Transactions
 */
class TransactionListPresenter {

    private List<Object> viewModels;

    public TransactionListPresenter() {
        viewModels = Arrays.asList(
                new TransactionListItemViewModel(R.drawable.ic_receipt, "Transction 1", "Subtitle", null),
                new HeaderListItemViewModel("Yesterday", null),
                new TransactionListItemViewModel(R.drawable.ic_receipt, "Transaction 2", null, null),
                new TransactionListItemViewModel(R.drawable.ic_receipt, "Transaction 3", "Subtitle", "$15.00"),
                new HeaderListItemViewModel("Thursday", null)
        );
    }

    @NonNull
    public List<Object> getTransactionListItems() {
        return new ArrayList<>(viewModels);
    }

}
