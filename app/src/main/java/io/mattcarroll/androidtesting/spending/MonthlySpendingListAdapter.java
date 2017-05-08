package io.mattcarroll.androidtesting.spending;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.mattcarroll.androidtesting.R;

class MonthlySpendingListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_CHART = 1;
    private static final int VIEW_TYPE_LIST_ITEM = 2;

    private static final int CHART_POSITION = 0;

    private List<ChartSectionViewModel> chartSectionViewModels;
    private List<SpendingListItemViewModel> listItemViewModels;

    MonthlySpendingListAdapter(@NonNull List<ChartSectionViewModel> chartSectionViewModels,
                               @NonNull List<SpendingListItemViewModel> listItemViewModels) {
        this.chartSectionViewModels = chartSectionViewModels;
        this.listItemViewModels = listItemViewModels;
    }

    @Override
    public int getItemCount() {
        return listItemViewModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == CHART_POSITION) {
            return VIEW_TYPE_CHART;
        } else {
            return VIEW_TYPE_LIST_ITEM;
        }
    }

    @Nullable
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_CHART) {
            viewHolder = createChartViewHolder(parent);
        } else if (viewType == VIEW_TYPE_LIST_ITEM) {
            viewHolder = createListItemViewHolder(parent);
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder createChartViewHolder(@NonNull ViewGroup parent) {
        View chartContainer = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centered_chart, parent, false);
        return new ChartViewHolder(chartContainer);
    }

    @NonNull
    private RecyclerView.ViewHolder createListItemViewHolder(@NonNull ViewGroup parent) {
        View listItemView = new SpendingListItemView(parent.getContext());
        return new ListItemViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListItemViewHolder) {
            ListItemViewHolder listItemViewHolder = (ListItemViewHolder) holder;
            SpendingListItemViewModel viewModel = getViewModelForPosition(position);
            listItemViewHolder.listItemView.setViewModel(viewModel);
        } else {
            ChartViewHolder chartViewHolder = (ChartViewHolder) holder;
            chartViewHolder.spendingChart.setViewModels(chartSectionViewModels);
        }
    }

    @NonNull
    private SpendingListItemViewModel getViewModelForPosition(int position) {
        return listItemViewModels.get(position - 1);
    }

    private static class ChartViewHolder extends RecyclerView.ViewHolder {

        final MonthlySpendingChart spendingChart;

        private ChartViewHolder(@NonNull View itemView) {
            super(itemView);

            spendingChart = (MonthlySpendingChart) itemView.findViewById(R.id.chart);
        }
    }

    private static class ListItemViewHolder extends RecyclerView.ViewHolder {

        final SpendingListItemView listItemView;

        private ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            listItemView = (SpendingListItemView) itemView;
        }
    }
}
