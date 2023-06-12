package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class DataStorageManager {
    private static final String TRANSACTION_FILE_PATH = "transactions.csv";
    private static final String CATEGORY_FILE_PATH = "categories.csv";

    private CategoryModel categories;
    private TransactionModel transactions;

    public CategoryModel getCategories(){
        return categories;
    }

    public TransactionModel getTransactions(){
        return transactions;
    }

    public static void saveTransaction(Transaction transaction) {
        // Logic to save a transaction to the transaction file
    }

    public static List<Transaction> loadTransactions() {
        // Logic to load transactions from the transaction file
        return null;
    }

    public static void editTransaction(Transaction transaction) {
        // Logic to edit a transaction in the transaction file
    }

    public static void deleteTransaction(Transaction transaction) {
        // Logic to delete a transaction from the transaction file
    }

    public static void saveCategory(Category category) {
        // Logic to save a category to the category file
    }

    public static List<Category> loadCategories() {
        // Logic to load categories from the category file
        return null;
    }

    public static void editCategory(Category category) {
        // Logic to edit a category in the category file
    }

    public static void deleteCategory(Category category) {
        // Logic to delete a category from the category file
    }
}
