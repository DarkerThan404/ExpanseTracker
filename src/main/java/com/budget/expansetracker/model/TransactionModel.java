package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionModel {
    private ObservableList<Transaction> transactions;
    private DataStorageManager dataStorageManager;

    public TransactionModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        transactions = FXCollections.observableArrayList();
    }

    public void add(Transaction transaction){
        transactions.add(transaction);
        dataStorageManager.addTransactionToFile(transaction);
    }

    public void remove(Transaction transaction){
        transactions.remove(transaction);
    }

    public ObservableList<Transaction> getTransactions(){
        return transactions;
    }
}
