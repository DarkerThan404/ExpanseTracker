package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionModel {
    private ObservableList<Transaction> transactions;
    private DataStorageManager dataStorageManager;

    private int nextID;

    public TransactionModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        transactions = FXCollections.observableArrayList();
        nextID = 0;
    }

    /**
     * Called once at start of the application to set nextID to highest possible number
     * @param nextID
     */
    public void setNextID(int nextID){ this.nextID = nextID;}

    public void add(Transaction transaction){
        transaction.setID(nextID++);
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
