package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountListPresenterTest {
    private static final String BANK_NAME = "Acme";
    private static final String ACCOUNT_NAME = "Super Deluxe Card";
    private static final String ACCOUNT_NUMBER = "0000111100001111";

    @Mock
    private AccountNumberMask mockMask;
    private AccountListPresenter presenter;
    private Collection<BankAccount> accounts;

    @Before
    public void setup() {
        when(mockMask.apply(any(String.class))).thenReturn(""); // fulfill @NonNull contract
        presenter = new AccountListPresenter(mockMask, null);
        accounts = new HashSet<>();
    }

    @Test
    public void itPresentsBankAndAccountNameForDisplayName() {
        BankAccount account = linkAccount(BANK_NAME, ACCOUNT_NAME);
        String expectedDisplayName = account.getBankName() + " " + account.getAccountName();

        List<AccountListItemViewModel> viewModels = presenter.present(accounts);

        String displayName = viewModels.get(0).getDisplayName();
        assertEquals(expectedDisplayName, displayName);
    }

    @Test
    public void itPresentsMaskedAccountNumber() {
        BankAccount account = linkAccountWithNumber(ACCOUNT_NUMBER);

        presenter.present(accounts);

        verify(mockMask).apply(account.getAccountId());
    }

    @NonNull
    private BankAccount linkAccount(@NonNull String bankName, @NonNull String accountName) {
        BankAccount account = new BankAccount(bankName, accountName, "", Collections.<Transaction>emptyList());
        accounts.add(account);
        return account;
    }

    @NonNull
    private BankAccount linkAccountWithNumber(@NonNull String accountNumber) {
        BankAccount account = new BankAccount("", "", accountNumber, Collections.<Transaction>emptyList());
        accounts.add(account);
        return account;
    }

}
