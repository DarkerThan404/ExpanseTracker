package com.budget.expansetracker;

public class Category {
    private int ID;
    private String name;
    private double current;
    private double goal;

    public Category(int ID, String name, double current, double goal){
        this.ID = ID;
        this.name = name;
        this.current = current;
        this.goal = goal;
    }

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getCurrent() { return current;}

    public double getGoal() {
        return goal;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrent(double current) { this.current = current; }

    public void setGoal(double goal) {
        this.goal = goal;
    }



    @Override
    public String toString() {
        return name;
    }

    public String toCsv() {
        return ID + "," + name + "," + current + "," + goal;
    }

    public static Category fromCsv(String csv) {
        String[] fields = csv.split(",");
        int ID = parseInteger(fields[0]);
        String name = validateName(fields[1]);
        double current = parsePositiveDouble(fields[2]);
        double goal = parsePositiveDouble(fields[3]);
        return new Category(ID, name, current, goal);
    }

    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value, e);
        }
    }

    private static String validateName(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        return value;
    }

    private static double parsePositiveDouble(String value) {
        try {
            double number = Double.parseDouble(value);
            if (number < 0) {
                throw new IllegalArgumentException("Value must be a positive double: " + value);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value: " + value, e);
        }
    }
}
