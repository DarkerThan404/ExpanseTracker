package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private ObservableList<Category> categories;
    private Category defaultCategory;

    public CategoryModel(){
        categories = FXCollections.observableArrayList();
        defaultCategory = new Category(-9999,"Uncategorized", 100,100);
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

    public Category getDefaultCategory(){
        return defaultCategory;
    }


}
