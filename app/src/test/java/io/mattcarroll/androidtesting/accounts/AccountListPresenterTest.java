package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountListPresenterTest {
    private AccountListPresenter presenter;
    private Set<BankAccount> accounts;

    @Before
    public void setup() {
        presenter = new AccountListPresenter(null);
        accounts = new HashSet<>();
    }

    @Test
    public void itPresentsBankAndAccountNameForDisplayName() {
        linkAccount("Acme", "Super Deluxe Card");
        String expectedDisplayName = "Acme Super Deluxe Card";

        List<AccountListItemViewModel> viewModels = presenter.present(accounts);

        String displayName = viewModels.get(0).displayName();
        assertEquals(expectedDisplayName, displayName);
    }

    @Test
    public void itMasksAccountNumber() {
        linkAccountWithNumber("0000111122223333");
        String expectedMaskedNumber = "************3333";

        List<AccountListItemViewModel> viewModels = presenter.present(accounts);

        String maskedAccountNumber = viewModels.get(0).accountNumber();
        assertEquals(expectedMaskedNumber, maskedAccountNumber);
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
