package io.mattcarroll.androidtesting.spending;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class MonthlySpendingChartPresenter {

    @NonNull
    public List<ChartSectionViewModel> presentChart() {
        return Arrays.asList(
                new ChartSectionViewModel(new BigDecimal(2500), "Housing"),
                new ChartSectionViewModel(new BigDecimal(1407), "Travel"),
                new ChartSectionViewModel(new BigDecimal(1324), "Entertainment"),
                new ChartSectionViewModel(new BigDecimal(1212), "Utilities"),
                new ChartSectionViewModel(new BigDecimal(1150), "Dining Out"),
                new ChartSectionViewModel(new BigDecimal(1121), "Transportation")
        );
    }
}
