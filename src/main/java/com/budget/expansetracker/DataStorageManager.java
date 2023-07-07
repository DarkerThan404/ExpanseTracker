package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public DataStorageManager(){
        loadDataFromFiles();
        calculateCurrentForAllCategories();
    }


    public CategoryModel getCategoryModel(){
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

    public void addCategoryToFile(Category category) {

        System.out.println("Saved category");

        // Append the new category's data to the categories file
        try {
            FileWriter fileWriter = new FileWriter(CATEGORY_FILE_PATH, true); // Open file in append mode
            String categoryData = category.toCsv(); // Get the CSV representation of the category
            fileWriter.write(categoryData + "\n"); // Write the data to the file followed by a new line
            fileWriter.close(); // Close the file
        } catch (IOException e) {
            // Handle any exceptions that occur during file writing
            System.out.println("Error writing data: " + e.getMessage());
        }
    }

    public void addTransactionToFile(Transaction transaction) {
        System.out.println("Saved transaction");

        // Append the new category's data to the categories file
        try {
            FileWriter fileWriter = new FileWriter(TRANSACTION_FILE_PATH, true); // Open file in append mode
            String categoryData = transaction.toCsv(); // Get the CSV representation of the category
            fileWriter.write(categoryData + "\n"); // Write the data to the file followed by a new line
            fileWriter.close(); // Close the file
        } catch (IOException e) {
            // Handle any exceptions that occur during file writing
            System.out.println("Error writing data: " + e.getMessage());
        }
    }

    public void deleteCategory(Category category) {
        // Remove the category from the category model
        categories.remove(category);

        // Update the category file
        updateCategoryFile();
    }

    private void updateCategoryFile() {
        try {
            saveCategoriesToFile(getCategoryModel().getCategories());
        } catch (IOException e) {
            // Handle the exception, e.g., log an error or show an error message
            e.printStackTrace();
        }
    }

    private void saveCategoriesToFile(List<Category> categories) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORY_FILE_PATH))) {
            for (Category category : categories) {
                String line = category.getID() + "," + category.getName() + "," + category.getCurrent() + "," + category.getGoal();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void loadCategoriesFromFile() throws IOException, URISyntaxException {
        categories = new CategoryModel(this);

        int highestID = 0;

        List<String> lines = Files.readAllLines(Paths.get(getDataFilePath(CATEGORY_FILE_PATH)));
        for (String line : lines) {
            Category category = Category.fromCsv(line);
            highestID = Math.max(highestID, category.getID());
            categories.getCategories().add(category);
        }
        categories.setNextID(highestID + 1);
    }

    private void loadTransactionsFromFile() throws IOException, URISyntaxException {
        transactions = new TransactionModel(this);
        int highestID = 0;
        List<String> lines = Files.readAllLines(Paths.get(getDataFilePath(TRANSACTION_FILE_PATH)));
        for (String line : lines) {
            Transaction transaction = Transaction.fromCsv(line,categories);
            highestID = Math.max(highestID, transaction.getID());
            transactions.getTransactions().add(transaction);
        }
        transactions.setNextID(highestID + 1);
    }

    public void loadDataFromFiles() {
        try {
            loadCategoriesFromFile();
            loadTransactionsFromFile();
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading data from files: " + e.getMessage());
        }
    }

    private String getDataFilePath(String fileName) {
        String workingDirectory = System.getProperty("user.dir");
        return workingDirectory + File.separator + fileName;
    }

    public void calculateCurrentForAllCategories() {
        for (Category category : categories.getCategories()) {
            double current = calculateCurrentForCategory(category);
            category.setCurrent(current);
        }
    }

    private double calculateCurrentForCategory(Category category) {
        double total = 0.0;
        for (Transaction transaction : transactions.getTransactions()) {
            if (transaction.getCategory() == category) {
                if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
                    total += transaction.getAmount();
                }
            }
        }
        return total;
    }

    public double calculateInitialBalance() {
        double incomeTotal = 0.0;
        double expenseTotal = 0.0;

        for (Transaction transaction : transactions.getTransactions()) {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                incomeTotal += transaction.getAmount();
            } else if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
                expenseTotal += transaction.getAmount();
            }
        }

        return incomeTotal - expenseTotal;
    }
}
