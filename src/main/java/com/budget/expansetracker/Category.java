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
    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

}
