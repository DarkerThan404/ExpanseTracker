package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class DataStorageManager {
    private static final String DATA_DIRECTORY = "BudgetPlanner";
    private static final String TRANSACTION_FILE_NAME = "transactions.csv";
    private static final String CATEGORY_FILE_NAME = "categories.csv";

    private String TRANSACTION_FILE_PATH;
    private String CATEGORY_FILE_PATH;

    private CategoryModel categories;
    private TransactionModel transactions;

    public DataStorageManager(){
        try {
            String dataDirectoryPath = createSubdirectory();
            TRANSACTION_FILE_PATH = dataDirectoryPath + File.separator + TRANSACTION_FILE_NAME;
            CATEGORY_FILE_PATH = dataDirectoryPath + File.separator + CATEGORY_FILE_NAME;
            loadDataFromFiles();
        } catch (IOException e) {
            System.out.println("Error creating subdirectory: " + e.getMessage());
        }
    }


    /**
     * Creates home directory
     * @return home directory
     */
    private String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * Function to create subdirectory in home directory
     * @return directory path
     */
    private String createSubdirectory() throws IOException {
        String homeDirectory = getHomeDirectory();
        String subdirectoryPath = homeDirectory + File.separator + DATA_DIRECTORY + File.separator + "data";

        File subdirectory = new File(subdirectoryPath);
        if (!subdirectory.exists()) {
            boolean success = subdirectory.mkdirs();
            if (!success) {
                throw new IOException("Failed to create the subdirectory.");
            }
        }
        return subdirectoryPath;
    }

    /**
     * Getter for category model
     * @return
     */
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

    /**
     * Getter for transaction model
     * @return
     */
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

    /**
     * Adds category to a file
     * @param category instance to add
     */
    public void addCategoryToFile(Category category) {
        // Append the new category's data to the categories file
        try {
            FileWriter fileWriter = new FileWriter(CATEGORY_FILE_PATH, true); // Open file in append mode
            String categoryData = category.toCsv(); // Get the CSV representation of the category
            fileWriter.write(categoryData + "\n"); // Write the data to the file followed by a new line
            fileWriter.close(); // Close the file
        } catch (IOException e) {
            System.out.println("Error writing data: " + e.getMessage());
        }
    }

    /**
     * Adds transaction to a file
     * @param transaction instance to add
     */
    public void addTransactionToFile(Transaction transaction) {
        // Append the new category's data to the categories file
        try {
            FileWriter fileWriter = new FileWriter(TRANSACTION_FILE_PATH, true); // Open file in append mode
            String categoryData = transaction.toCsv(); // Get the CSV representation of the category
            fileWriter.write(categoryData + "\n"); // Write the data to the file followed by a new line
            fileWriter.close(); // Close the file
        } catch (IOException e) {
            System.out.println("Error writing data: " + e.getMessage());
        }
    }

    /**
     * Deletes category from a file
     * @param category instance to delete
     */
    public void deleteCategory(Category category) {
        // Remove the category from the category model
        categories.remove(category);

        // Update the category file
        updateCategoryFile();
    }

    /**
     * Updates category file when categories are modified
     */
    public void updateCategoryFile() {
        try {
            saveCategoriesToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves categories to a file
     * @throws IOException
     */
    private void saveCategoriesToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORY_FILE_PATH))) {
            for (Category category : getCategoryModel().getCategories()) {
                String line = category.toCsv();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Removes transactions from list and saves tem to a file
     * @param selectedTransactions
     */
    public void removeTransactions(List<Transaction> selectedTransactions) {
        transactions.removeTransactions(selectedTransactions);
        try {
            saveTransactionsToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates transaction file
     */
    public void updateTransactionFile(){
        try {
            saveTransactionsToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores transactions to a file
     */
    private void saveTransactionsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE_PATH))) {
            // Iterate through the transactions in the data model and write them to the file
            for (Transaction transaction : transactions.getTransactions()) {
                String line = transaction.toCsv();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Loads categories from file
     * @throws IOException
     * @throws URISyntaxException
     */
    private void loadCategoriesFromFile() throws IOException, URISyntaxException {
        categories = new CategoryModel(this);

        int highestID = 0;

        Path categoriesFilePath = Paths.get(CATEGORY_FILE_PATH);
        if (!Files.exists(categoriesFilePath)) {
            // If the file does not exist, create it
            Files.createFile(categoriesFilePath);
        } else {
            // If the file exists, read its contents
            List<String> lines = Files.readAllLines(categoriesFilePath);
            for (String line : lines) {
                Category category = Category.fromCsv(line);
                highestID = Math.max(highestID, category.getID());
                categories.getCategories().add(category);
            }
        }
        categories.setNextID(highestID + 1);
    }

    /**
     * Loads transactions from file
     * @throws IOException
     * @throws URISyntaxException
     */
    private void loadTransactionsFromFile() throws IOException, URISyntaxException {
        transactions = new TransactionModel(this);
        int highestID = 0;
        Path transactionsFilePath = Paths.get(TRANSACTION_FILE_PATH);
        if (!Files.exists(transactionsFilePath)) {
            // If the file does not exist, create it
            Files.createFile(transactionsFilePath);
        } else {
            // If the file exists, read its contents
            List<String> lines = Files.readAllLines(transactionsFilePath);
            for (String line : lines) {
                Transaction transaction = Transaction.fromCsv(line, categories);
                highestID = Math.max(highestID, transaction.getID());
                transactions.getTransactions().add(transaction);
            }
        }
        transactions.setNextID(highestID + 1);
    }

    /**
     * Loads data from data files
     */
    public void loadDataFromFiles() {
        try {
            loadCategoriesFromFile();
            loadTransactionsFromFile();
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading data from files: " + e.getMessage());
        }
    }

    /**
     * Makes data file path
     * @param fileName file name
     * @return full path to data file
     */
    private String getDataFilePath(String fileName) {
        String workingDirectory = System.getProperty("user.dir");
        return workingDirectory + File.separator + fileName;
    }

    /**
     * Resets values of current for categories
     */
    public void resetCurrentValues() {
        for (Category category : categories.getCategories()) {
            category.setCurrent(0.0);
        }
    }

    /**
     * Function that calculate current values for current month.
     * Used in overview
     */
    public void calculateCurrentForCurrentMonth() {

        resetCurrentValues();
        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        for (Category category : categories.getCategories()) {
            double current = 0.0;

            for (Transaction transaction : transactions.getTransactions()) {
                LocalDate transactionDate = transaction.getDate();
                int transactionMonth = transactionDate.getMonthValue();
                int transactionYear = transactionDate.getYear();

                if (transactionMonth == currentMonth && transactionYear == currentYear && transaction.getCategory() == category && transaction.getType() == Transaction.TransactionType.EXPENSE) {
                    current += transaction.getAmount();
                }
            }
            category.setCurrent(current);
        }
    }
}
