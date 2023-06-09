package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private ObservableList<Category> categories;

    public CategoryModel(){
        categories = FXCollections.observableArrayList();
    }

    public void add(Category category){
        categories.add(category);
    }

    public void prepend(Category category){
        categories.add(0,category);
    }

    public void remove(Category category){
        categories.remove(category);
    }

    public ObservableList<Category> getCategories(){
        return categories;
    }
}
