package com.budget.expansetracker;

import com.budget.expansetracker.model.CategoryModel;

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

    public static Transaction fromCsv(String csv, CategoryModel categories) {
        String[] values = csv.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[1];
        LocalDate date = LocalDate.parse(values[2]);
        double amount = Double.parseDouble(values[3]);
        TransactionType type = TransactionType.valueOf(values[4]);
        int categoryId = Integer.parseInt(values[5]);
        Category category = categories.getCategoryByID(categoryId);
        String description = values[6];

        return new Transaction(id, name, date, amount, type, category, description);
    }
}
