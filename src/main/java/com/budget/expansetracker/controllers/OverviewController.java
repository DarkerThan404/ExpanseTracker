package com.budget.expansetracker.controllers;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.OverviewView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class OverviewController implements IController {

    private OverviewView view;

    private static int nextID;

    private CategoryModel categories;
    private TransactionModel transactions;

    public OverviewController(CategoryModel categoryModel, TransactionModel transactionModel){
        this.categories = categoryModel;
        this.transactions = transactionModel;
        view = new OverviewView(this, categoryModel, transactionModel);
    }

    public void handleAddCategoryButton(ActionEvent event){
        // Create the dialog
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter the category name:");

        // Set the dialog content
        TextField categoryNameField = new TextField();
        TextField goalField = new TextField();
        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Category Name:"), categoryNameField);
        grid.addRow(1, new Label("Goal:"), goalField);
        dialog.getDialogPane().setContent(grid);

        // Add buttons to the dialog
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

        // Set the result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                String categoryName = categoryNameField.getText();
                double goal;

                try {
                    goal = Double.parseDouble(goalField.getText());
                } catch (NumberFormatException e) {
                    // Handle the error
                    System.err.println("Invalid goal value: " + goalField.getText());
                    return null;
                }

                boolean isDuplicate = categories.getCategories().stream().anyMatch(category -> category.getName().equalsIgnoreCase(categoryName));
                if (isDuplicate) {
                    // Handle the duplicate name
                    System.err.println("Category name already exists: " + categoryName);
                    return null;
                }
                int ID = nextID++;
                return new Category(ID, categoryName, 0,  goal);
            }
            return null;
        });

        // Show the dialog and handle the user's input
        Optional<Category> result = dialog.showAndWait();
        result.ifPresent(category -> {
            // This code will be executed when the user clicks the "Add" button
            categories.add(category);
        });
    }

    @Override
    public Node getView() {
        return view.getNode();
    }
}
