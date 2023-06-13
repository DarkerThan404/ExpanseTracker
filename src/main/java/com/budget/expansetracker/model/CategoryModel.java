package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private ObservableList<Category> categories;
    private Category defaultCategory;
    private DataStorageManager dataStorageManager;

    public CategoryModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        categories = FXCollections.observableArrayList();
        defaultCategory = new Category(-9999,"Uncategorized", 100,100);
    }

    /**
     * Adds category to category list and writes it into a file. For adding without saving
     * to file use getCategories first
     * @param category
     */
    public void add(Category category){
        categories.add(category);
        dataStorageManager.addCategoryToFile(category);
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

    public Category getCategoryByID(int categoryID){
        for (Category category : categories) {
            if (category.getID() == categoryID) {
                return category;
            }
        }
        return null; // Category not found
    }

    public Category getDefaultCategory() {
        return defaultCategory;
    }
}
