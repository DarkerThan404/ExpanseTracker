package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataStorageManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String TRANSACTION_FILE_PATH = DATA_DIRECTORY + File.separator + "transactions.csv";
    private static final String CATEGORY_FILE_PATH = DATA_DIRECTORY + File.separator + "categories.csv";

    private CategoryModel categories;
    private TransactionModel transactions;

    public CategoryModel getCategoryModel(){
        System.out.println(System.getProperty("user.dir"));
        if(categories == null){
            try {
                loadCategoriesFromFile();
                System.out.println("Data loaded successfully.");
            } catch (IOException | URISyntaxException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
        return categories;
    }

    public TransactionModel getTransactionModel(){
        if(transactions == null){
            try {
                loadTransactionsFromFile();
                System.out.println("Data loaded successfully.");
            } catch (IOException | URISyntaxException e) {
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
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void loadCategoriesFromFile() throws IOException, URISyntaxException {
        categories = new CategoryModel();
        List<String> lines = Files.readAllLines(Paths.get(getDataFilePath(CATEGORY_FILE_PATH)));
        for (String line : lines) {
            Category category = Category.fromCsv(line);
            categories.add(category);
        }
    }

    private void loadTransactionsFromFile() throws IOException, URISyntaxException {
        transactions = new TransactionModel();
        List<String> lines = Files.readAllLines(Paths.get(getDataFilePath(TRANSACTION_FILE_PATH)));
        for (String line : lines) {
            Transaction transaction = Transaction.fromCsv(line,categories);
            transactions.add(transaction);
        }
    }

    private String getDataFilePath(String fileName) {
        String workingDirectory = System.getProperty("user.dir");
        return workingDirectory + File.separator + fileName;
    }
}
