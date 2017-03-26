package io.mattcarroll.androidtesting.spending;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.mattcarroll.androidtesting.R;

public class MonthlySpendingActivity extends AppCompatActivity {

    private MonthlySpendingChart monthlySpendingChart;
    private MonthlySpendingChartPresenter chartPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_spending);

        monthlySpendingChart = (MonthlySpendingChart) findViewById(R.id.monthly_spending_chart);
        chartPresenter = new MonthlySpendingChartPresenter();
        monthlySpendingChart.setViewModels(chartPresenter.presentChart());
    }
}
