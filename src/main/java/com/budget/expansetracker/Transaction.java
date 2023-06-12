package com.budget.expansetracker;

import java.time.LocalDate;
import java.util.StringJoiner;

public class Transaction {
    private int ID;
    private String name;
    private LocalDate date;
    private double amount;
    private TransactionType type;
    private Category category;
    private String description;

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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toCsvString() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(String.valueOf(ID));
        joiner.add(name);
        joiner.add(date.toString());
        joiner.add(String.valueOf(amount));
        joiner.add(category.getName());
        joiner.add(type.toString());
        joiner.add(description);
        return joiner.toString();
    }

    public static Transaction fromCsvString(String csvString) {
        String[] fields = csvString.split(",");
        int ID = Integer.parseInt(fields[0]);
        String name = fields[1];
        LocalDate date = LocalDate.parse(fields[2]);
        double amount = Double.parseDouble(fields[3]);
        Category category = null; //new Category(fields[4]);
        TransactionType type = TransactionType.valueOf(fields[5]);
        String description = fields[6];

        return new Transaction(ID, name, date, amount,  type, category, description);
    }
}
