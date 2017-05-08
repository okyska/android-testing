package io.mattcarroll.androidtesting.creditcardanalysis;

/**
 * Presents credit card analysis information including aggregate balances and utilization
 * amount.
 */
public class CreditCardAnalysisPresenter {

    public CreditCardBalanceViewModel getCreditCardBalances() {
        return new CreditCardBalanceViewModel(85000, null);
    }

    public UtilizationOfCreditLimitViewModel getUtilizationOfCreditLimit() {
        return new UtilizationOfCreditLimitViewModel(108700, 550000);
    }

}
