package io.mattcarroll.androidtesting.spending;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.mattcarroll.androidtesting.R;

public class MonthlySpendingActivity extends AppCompatActivity {

    private MonthlySpendingChartPresenter chartPresenter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_spending);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        chartPresenter = new MonthlySpendingChartPresenter();
        List<ChartSectionViewModel> chartSectionViewModels = chartPresenter.presentChart();

        SpendingListPresenter listPresenter = new SpendingListPresenter();
        List<SpendingListItemViewModel> listItemViewModels = listPresenter.presentList(getResources());

        MonthlySpendingListAdapter adapter = new MonthlySpendingListAdapter(chartSectionViewModels, listItemViewModels);

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

}
