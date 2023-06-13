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
        int ID = Integer.parseInt(fields[0]);
        String name = fields[1];
        double current = Double.parseDouble(fields[2]);
        double goal = Double.parseDouble(fields[3]);
        return new Category(ID, name, current, goal);
    }
}
