package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO
 */
public class AccountsApi {

    private static AccountsApi instance;

    public static AccountsApi getInstance() {
        if (null == instance) {
            instance = new AccountsApi();
        }
        return instance;
    }

    private BankAccountRepository bankAccountRepository;

    private AccountsApi() {
        bankAccountRepository = new BankAccountRepository();
    }

    @NonNull
    public Set<String> accountIds() {
        return bankAccountRepository.getBankAccountIds();
    }

    @NonNull
    public Set<BankAccount> accounts() {
        return bankAccountRepository.getBankAccounts();
    }

    public void linkBankAccount(@NonNull AccountCredentials accountCredentials) {
        BankAccount linkedBankAccount = doLinkAccount(accountCredentials);
        bankAccountRepository.addBankAccount(linkedBankAccount);
    }

    private BankAccount doLinkAccount(@NonNull AccountCredentials accountCredentials) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String institutionName = accountCredentials.getFinancialInstitutionName();

        return new BankAccount(
                institutionName,
                new RandomAccountName().generate(institutionName),
                accountCredentials.getAccountNumber(),
                new RandomTransactions().generate());
    }

    public void removeAccount(@NonNull String bankAccountId) {
        bankAccountRepository.removeBankAccount(bankAccountId);
    }

    public int balanceInCents(@NonNull String bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.getBankAccount(bankAccountId);
        if (null == bankAccount) {
            throw new IllegalStateException("No such bank account: " + bankAccountId);
        }

        int balance = 0;
        for (Transaction transaction : bankAccount.getAllTransactions()) {
            balance += transaction.getAmountInCents();
        }
        return balance;
    }

    @NonNull
    public List<Transaction> listTransactions(@NonNull String ... bankAccountIds) {
        return listTransactions(Long.MIN_VALUE, Long.MAX_VALUE, bankAccountIds);
    }

    @NonNull
    public List<Transaction> listTransactions(long startTime, long endTime, @NonNull String ... bankAccountIds) {
        List<Transaction> transactions = new ArrayList<>();
        for (String accountId : bankAccountIds) {
            BankAccount bankAccount = bankAccountRepository.getBankAccount(accountId);
            transactions.addAll(bankAccount.getTransactionsInDateRange(startTime, endTime));
        }
        return transactions;
    }
}
