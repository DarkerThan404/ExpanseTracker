package com.budget.expansetracker.model;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private ObservableList<Category> categories;

    private Category defaultCategory;
    private DataStorageManager dataStorageManager;

    private int nextID;

    public CategoryModel(DataStorageManager dataStorageManager){
        this.dataStorageManager = dataStorageManager;
        categories = FXCollections.observableArrayList();
        defaultCategory = new Category(-9999,"Uncategorized", 100,100);
        nextID = 0;
    }

    /**
     * Adds category to category list and writes it into a file. For adding without saving
     * to file use getCategories first
     * @param category
     */
    public void add(Category category){
        category.setId(nextID++);
        categories.add(category);
        dataStorageManager.addCategoryToFile(category);
    }

    /**
     * Called once at start of the application to set nextID to highest possible number
     * @param nextID
     */
    public void setNextID(int nextID){ this.nextID = nextID;}

    /**
     * Removes category from the lsit
     * @param category instance to remove
     */
    public void remove(Category category){
        categories.remove(category);
    }

    /**
     * Getter for categories
     * @return
     */
    public ObservableList<Category> getCategories(){
        return categories;
    }

    /**
     * Function that return category based on its ID
     * @param categoryID key for finding category
     * @return category
     */
    public Category getCategoryByID(int categoryID){
        for (Category category : categories) {
            if (category.getID() == categoryID) {
                return category;
            }
        }
        if(categoryID == defaultCategory.getID()){
            return defaultCategory;
        }
        return null; // Category not found
    }

    /**
     * Getter for default category
     * @return
     */
    public Category getDefaultCategory() {
        return defaultCategory;
    }
}
