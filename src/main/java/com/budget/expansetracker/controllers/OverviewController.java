package com.budget.expansetracker.controllers;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.OverviewView;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OverviewController implements IController {

    private OverviewView view;

    private static int nextID;

    private DataStorageManager storageManager;

    private CategoryModel categories;
    private TransactionModel transactions;

    public OverviewController(DataStorageManager storageManager){
        this.storageManager = storageManager;
        categories = storageManager.getCategoryModel();
        transactions = storageManager.getTransactionModel();
        view = new OverviewView(this, categories, transactions);
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

    public List<Transaction> getRecentTransactions(int recentTransactionCount) {
        List<Transaction> allTransactions = transactions.getTransactions();
        Collections.sort(allTransactions, (t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        List<Transaction> recentTransactions = new ArrayList<>();
        int numRecentTransactions = 5;
        for (int i = 0; i < numRecentTransactions && i < allTransactions.size(); i++){
            recentTransactions.add(allTransactions.get(i));
        }
        return recentTransactions;
    }

    public void handleEditCategory(Category category) {
        System.out.println(category.getName());
        System.out.println(category.getGoal());
        Dialog<Category> dialog = createEditCategoryDialog(category);
        Optional<Category> result = dialog.showAndWait();
        if(result.isPresent()){
            Category editedCategory = result.get();

            // Update the current and goal properties with the new values
            category.setName(editedCategory.getName());
            category.setCurrent(editedCategory.getCurrent());
            category.setGoal(editedCategory.getGoal());
            System.out.println(category.getName());
            System.out.println(category.getGoal());
            view.updateCategoryBoxInList(category);

        }

    }

    private Dialog<Category> createEditCategoryDialog(Category category){
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Edit category");
        dialog.setHeaderText(null);
        dialog.setResizable(false);

        TextField nameTextField = new TextField(category.getName());
        TextField goalTextField = new TextField(String.valueOf(category.getGoal()));

        Label nameErrorLabel = new Label();
        Label goalErrorLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Name:"), nameTextField);
        GridPane.setColumnSpan(nameErrorLabel,2);
        gridPane.addRow(1,nameErrorLabel);

        gridPane.addRow(2, new Label("Goal:"), goalTextField);
        GridPane.setColumnSpan(goalErrorLabel, 2);
        gridPane.addRow(3,goalErrorLabel);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String nameText = nameTextField.getText().trim();
            String goalText = goalTextField.getText().trim();
            boolean isNameValid = !nameText.isEmpty();
            boolean isGoalValid = goalText.matches("^\\d*\\.?\\d+$");

            saveButton.setDisable(!isNameValid || !isGoalValid);

            nameErrorLabel.setText(isNameValid ? "" : "Name is required");
            goalErrorLabel.setText(isGoalValid ? "" : "Goal must be a positive number");
        };

        nameTextField.textProperty().addListener(validationListener);
        goalTextField.textProperty().addListener(validationListener);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameTextField.getText().trim();
                String goalText = goalTextField.getText().trim();
                double goal = Double.parseDouble(goalText);
                category.setGoal( goal);
                category.setName(name);
                return category;
            }
            return null;
        });

        return dialog;
    }

    public void handleDeleteCategory(Category category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Category");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this category?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Update the transactions to remove the reference to the deleted category

            for (Transaction transaction : transactions.getTransactions()) {
                if (transaction.getCategory().equals(category)) {
                    transaction.setCategory(categories.getDefaultCategory()); // Set category to null or assign a default category
                }
            }

            // Removes the category box from the UI and data structure
            storageManager.deleteCategory(category);
        }
    }
}
