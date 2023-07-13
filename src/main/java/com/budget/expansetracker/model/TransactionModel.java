package com.budget.expansetracker.model;

import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class TransactionModel {
    private final ObservableList<Transaction> transactions;
    private final DataStorageManager dataStorageManager;

    private int nextID;

    private double balance;

    public TransactionModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        transactions = FXCollections.observableArrayList();
        nextID = 0;
        balance = 0;
    }

    /**
     * Called once at start of the application to set nextID to the highest possible number
     * @param nextID Is passed as next id
     */
    public void setNextID(int nextID){ this.nextID = nextID;}

    /**
     * Getter for balance
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Function that calculates initial balance
     */
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

    /**
     * Function that adds transaction to list
     * @param transaction instance to add
     */
    public void add(Transaction transaction){
        transaction.setID(nextID++);
        transactions.add(transaction);
        dataStorageManager.addTransactionToFile(transaction);
    }

    public void removeTransactions(List<Transaction> selectedTransactions) {
        transactions.removeAll(selectedTransactions);
    }
    /**
     * Getter for transactions
     * @return
     */
    public ObservableList<Transaction> getTransactions(){
        return transactions;
    }

}
