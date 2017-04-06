package io.mattcarroll.androidtesting.spending;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.mattcarroll.androidtesting.R;

public class MonthlySpendingChart extends FrameLayout {
    private static final float SPLICE_SPACE = 3f;
    private static final float SELECTION_SHIFT = 5f;
    private static final float VALUE_TEXT_SIZE = 11f;

    private PieChart pieChart;

    public MonthlySpendingChart(@NonNull Context context) {
        this(context, null);
    }

    public MonthlySpendingChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.monthly_spending_chart, this, true);
        pieChart = (PieChart) findViewById(R.id.piechart);
        Description description = new Description();
        description.setText("Spending Breakdown");
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setUsePercentValues(true);
    }

    public void setViewModels(List<ChartSectionViewModel> viewModels) {
        clearChartValues();
        PieDataSet dataSet = initDataSet(viewModels);
        renderData(dataSet);
    }

    private void clearChartValues() {
        if (pieChart.getData() != null) {
            pieChart.clearValues();
        }
    }

    @NonNull
    private PieDataSet initDataSet(@NonNull List<ChartSectionViewModel> viewModels) {
        List<PieEntry> entries = new ArrayList<>(viewModels.size());

        for (ChartSectionViewModel viewModel : viewModels) {
            PieEntry entry = toPieEntry(viewModel);
            entries.add(entry);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Monthly Spending Breakdown");

        dataSet.setSliceSpace(SPLICE_SPACE);
        dataSet.setSelectionShift(SELECTION_SHIFT);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        return dataSet;
    }

    @NonNull
    private PieEntry toPieEntry(@NonNull ChartSectionViewModel viewModel) {
        return new PieEntry(viewModel.getCashAmount().floatValue(), viewModel.getCategory());
    }

    @NonNull
    private PieData createPieData(@NonNull PieDataSet dataSet) {
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(VALUE_TEXT_SIZE);
        return data;
    }

    private void renderData(@NonNull PieDataSet dataSet) {
        PieData data = createPieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

}
