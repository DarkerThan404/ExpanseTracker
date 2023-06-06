package com.budget.expansetracker;

import java.util.ArrayList;
import java.util.List;

public class DataStorageManager {
    private static final String TRANSACTION_FILE_PATH = "transactions.csv";
    private static final String CATEGORY_FILE_PATH = "categories.csv";

    private static List<Category> categories = new ArrayList<>();

    public static Category findCategoryByName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
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
