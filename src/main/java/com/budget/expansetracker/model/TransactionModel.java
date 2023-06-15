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

    private double balance;

    public TransactionModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        transactions = FXCollections.observableArrayList();
        nextID = 0;
        balance = 0;
    }

    /**
     * Called once at start of the application to set nextID to highest possible number
     * @param nextID
     */
    public void setNextID(int nextID){ this.nextID = nextID;}

    public double getBalance() {
        return balance;
    }

    public void calculateInitialBalance() {
        balance = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                balance += transaction.getAmount();
            } else {
                balance -= transaction.getAmount();
            }
        }
    }

    private void updateBalance(Transaction transaction) {
        if (transaction.getType() == Transaction.TransactionType.INCOME) {
            balance += transaction.getAmount();
        } else {
            balance -= transaction.getAmount();
        }
    }

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
