package io.mattcarroll.androidtesting.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.mattcarroll.androidtesting.Bus;

/**
 * Interface for retrieving and manipulating Bank Accounts and the
 * information associated with them. {@code AccountsApi} should be obtained
 * via {@link AccountsService}.
 */
public class AccountsApi {

    private static final String TAG = "AccountsApi";

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

    @WorkerThread
    public void linkBankAccount(@NonNull AccountCredentials accountCredentials) {
        BankAccount linkedBankAccount = doLinkAccount(accountCredentials);
        bankAccountRepository.addBankAccounts(linkedBankAccount);
        Bus.getBus().post(new BankAccountsChangedEvent());
    }

    @WorkerThread
    private BankAccount doLinkAccount(@NonNull AccountCredentials accountCredentials) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return BankAccountsDatasource.getInstance().linkAccount(accountCredentials);
    }

    public void removeAccount(@NonNull String bankAccountId) {
        bankAccountRepository.removeBankAccount(bankAccountId);
        Bus.getBus().post(new BankAccountsChangedEvent());
    }

    public void removeAllAccounts() {
        bankAccountRepository.removeAllBankAccounts();
        Bus.getBus().post(new BankAccountsChangedEvent());
    }

    public int balanceInCents(@NonNull String bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.getBankAccount(bankAccountId);
        if (null == bankAccount) {
            throw new IllegalStateException("No such bank account: " + bankAccountId);
        }

        int balance = 0;
        for (Transaction transaction : bankAccount.allTransactions()) {
            balance += transaction.amountInCents();
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
            transactions.addAll(bankAccount.transactionsInDateRange(startTime, endTime));
        }
        return transactions;
    }

    // For the persistence system to restore bank accounts without having to link them.
    void addAccounts(@NonNull BankAccount ... bankAccounts) {
        bankAccountRepository.addBankAccounts(bankAccounts);
        Bus.getBus().post(new BankAccountsChangedEvent());
    }

    void loadAccountsFromFile(@NonNull File file) throws FileNotFoundException {
        Log.d(TAG, "Loading accounts from file: " + file.getAbsolutePath());
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(file));
        Set<BankAccount> accounts = gson.fromJson(reader, new TypeToken<Set<BankAccount>>() { }.getType());
        Log.d(TAG, "Loaded " + accounts.size() + " accounts from file.");
        addAccounts(accounts.toArray(new BankAccount[] { }));
    }

    void writeAccountsToFile(@NonNull File file) throws IOException {
        Gson gson = new Gson();
        String accountsJson = gson.toJson(accounts());
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
        jsonWriter.jsonValue(accountsJson);
        jsonWriter.close();
    }
}
