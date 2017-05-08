package io.mattcarroll.androidtesting.spending;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.mattcarroll.androidtesting.R;

public class MonthlySpendingFragment extends Fragment {

    private MonthlySpendingChartPresenter chartPresenter;
    private RecyclerView recyclerView;

    @NonNull
    public static MonthlySpendingFragment newInstance() {
        return new MonthlySpendingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly_spending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_monthly_spending);

        chartPresenter = new MonthlySpendingChartPresenter();
        List<ChartSectionViewModel> chartSectionViewModels = chartPresenter.presentChart();

        SpendingListPresenter listPresenter = new SpendingListPresenter();
        List<SpendingListItemViewModel> listItemViewModels = listPresenter.presentList(getResources());

        MonthlySpendingListAdapter adapter = new MonthlySpendingListAdapter(chartSectionViewModels, listItemViewModels);

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }
}
