package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataStorageManager {
    private static final String TRANSACTION_FILE_PATH = "transactions.csv";
    private static final String CATEGORY_FILE_PATH = "categories.csv";

    private CategoryModel categories;
    private TransactionModel transactions;

    public CategoryModel getCategories(){
        if(categories == null){
            try {
                loadCategoriesFromFile();
                System.out.println("Data loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
        return categories;
    }

    public TransactionModel getTransactions(){
        if(transactions == null){
            try {
                loadTransactionsFromFile();
                System.out.println("Data loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
        return transactions;
    }

    public void saveDataToFile() {
        try {
            saveCategoriesToFile();
            saveTransactionsToFile();
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void saveCategoriesToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATEGORY_FILE_PATH))) {
            for (Category category : categories.getCategories()) {
                writer.println(category.toCsv());
            }
        }
    }

    private void saveTransactionsToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE_PATH))) {
            for (Transaction transaction : transactions.getTransactions()) {
                writer.println(transaction.toCsv());
            }
        }
    }

    public void loadDataFromFile() {
        try {
            loadCategoriesFromFile();
            loadTransactionsFromFile();
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void loadCategoriesFromFile() throws IOException {
        categories = new CategoryModel();
        List<String> lines = Files.readAllLines(Paths.get(CATEGORY_FILE_PATH));
        for (String line : lines) {
            Category category = Category.fromCsv(line);
            categories.add(category);
        }
    }

    private void loadTransactionsFromFile() throws IOException {
        transactions = new TransactionModel();
        List<String> lines = Files.readAllLines(Paths.get(TRANSACTION_FILE_PATH));
        for (String line : lines) {
            Transaction transaction = Transaction.fromCsv(line,categories);
            transactions.add(transaction);
        }
    }
}
