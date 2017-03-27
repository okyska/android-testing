package io.mattcarroll.androidtesting.creditcardanalysis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mattcarroll.androidtesting.R;

/**
 * Provides aggregate analysis information about all of the user's credit cards.
 */
public class CreditCardAnalysisFragment extends Fragment {

    private CreditCardBalancesView creditCardBalancesView;
    private UtilizationOfCreditLimitView utilizationOfCreditLimitView;

    private CreditCardAnalysisPresenter presenter;

    @NonNull
    public static CreditCardAnalysisFragment newInstance() {
        return new CreditCardAnalysisFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credit_card_analysis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new CreditCardAnalysisPresenter();

        creditCardBalancesView = (CreditCardBalancesView) view.findViewById(R.id.creditcardbalancesview);
        utilizationOfCreditLimitView = (UtilizationOfCreditLimitView) view.findViewById(R.id.utilizationofcreditlimitview);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePresentation();
    }

    private void updatePresentation() {
        creditCardBalancesView.setViewModel(presenter.getCreditCardBalances());
        utilizationOfCreditLimitView.setViewModel(presenter.getUtilizationOfCreditLimit());
    }
}
