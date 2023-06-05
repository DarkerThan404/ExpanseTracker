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



}
