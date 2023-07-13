package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.StringJoiner;

public class Transaction {
    private int ID;
    private String name;
    private LocalDate date;
    private double amount;
    private TransactionType type;
    private Category category;
    private String description;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public enum TransactionType {
        INCOME,
        EXPENSE
    }

    public Transaction(int ID, String name, LocalDate date, double amount, TransactionType type, Category category, String description) {
        this.ID = ID;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        isSelected = false;
    }

    // Getters
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() { return type; }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Helper function to convert transaction object to a csv representation
     * @return
     */
    public String toCsv() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(Integer.toString(ID));
        sj.add(name);
        sj.add(date.toString());
        sj.add(Double.toString(amount));
        sj.add(type.name());
        sj.add(Integer.toString(category.getID()));
        sj.add(description);
        return sj.toString();
    }

    /**
     * Convert csv representation to transaction object
     * @param csv instance string in csv representation
     * @param categories category model
     * @return transaction object
     */
    public static Transaction fromCsv(String csv, CategoryModel categories) {
        String[] values = csv.split(",");
        int id = parseInteger(values[0]);
        String name = validateName(values[1]);
        LocalDate date = parseDate(values[2]);
        double amount = parsePositiveDouble(values[3]);
        TransactionType type = parseTransactionType(values[4]);
        int categoryId = parseInteger(values[5]);
        Category category = categories.getCategoryByID(categoryId);
        String description = null;

        if (values.length > 6 && !values[6].isEmpty()) {
            description = values[6];
        }

        return new Transaction(id, name, date, amount, type, category, description);
    }

    /**
     * Helper function that parses integer
     * @param value to parse
     * @return parsed value
     */
    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value, e);
        }
    }

    /**
     * Validation function for name
     * @param value to validate
     * @return valid string
     */
    private static String validateName(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Invalid name: " + value);
        }
        return value;
    }

    /**
     * Parses string to local date object
     * @param value to parse
     * @return parsed local date object
     */
    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date value: " + value, e);
        }
    }

    /**
     * Parses and validates value to positive double
     * @param value to parse
     * @return parsed value
     */
    private static double parsePositiveDouble(String value) {
        try {
            double amount = Double.parseDouble(value);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive double: " + value);
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value: " + value, e);
        }
    }

    /**
     * Parses transaction type from string
     * @param value to parse
     * @return type
     */
    private static TransactionType parseTransactionType(String value) {
        try {
            return TransactionType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + value, e);
        }
    }
}
