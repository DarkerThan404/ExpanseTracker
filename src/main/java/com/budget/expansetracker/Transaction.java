package com.budget.expansetracker;

import java.time.LocalDate;

public class Transaction {
    private int ID;
    private String name;
    private LocalDate date;
    private double amount;
    private Category category;
    private String description;

    public Transaction(int ID, String name, LocalDate date, double amount, Category category, String description) {
        this.ID = ID;
        this.name = name;
        this.date = date;
        this.amount = amount;
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
}
