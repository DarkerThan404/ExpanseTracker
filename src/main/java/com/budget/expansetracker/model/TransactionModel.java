package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionModel {
    private ObservableList<Transaction> transactions;

    public TransactionModel(){
        transactions = FXCollections.observableArrayList();
    }

    public void add(Transaction transaction){
        transactions.add(transaction);
    }

    public void remove(Transaction transaction){
        transactions.remove(transaction);
    }

    public ObservableList<Transaction> getTransactions(){
        return transactions;
    }
}
