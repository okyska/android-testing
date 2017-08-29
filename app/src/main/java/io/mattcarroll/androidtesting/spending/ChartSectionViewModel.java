package io.mattcarroll.androidtesting.spending;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

class ChartSectionViewModel {
    private final BigDecimal cashAmount;
    private final String category;

    ChartSectionViewModel(@NonNull BigDecimal cashAmount, @NonNull String category) {
        this.cashAmount = cashAmount;
        this.category = category;
    }

    @NonNull
    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    @NonNull
    public String getCategory() {
        return category;
    }
}
