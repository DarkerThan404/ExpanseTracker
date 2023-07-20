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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OverviewController implements IController {

    private final OverviewView view;

    private static int nextID;

    private final DataStorageManager storageManager;

    private final CategoryModel categories;
    private final TransactionModel transactions;

    public OverviewController(DataStorageManager storageManager){
        this.storageManager = storageManager;
        categories = storageManager.getCategoryModel();
        transactions = storageManager.getTransactionModel();
        view = new OverviewView(this, categories, transactions);
    }

    /**
     * Function that handles adding category
     * @param event
     */
    public void handleAddCategory(ActionEvent event) {
        Dialog<Category> dialog = createAddCategoryDialog();
        Optional<Category> result = dialog.showAndWait();

        result.ifPresent(categories::add);
    }

    /**
     * Creates dialog for adding category
     * @return dialog
     */
    private Dialog<Category> createAddCategoryDialog() {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter the category name:");

        TextField categoryNameField = new TextField();
        TextField goalField = new TextField();
        GridPane grid = createCategoryDialogGrid(categoryNameField, goalField);
        dialog.getDialogPane().setContent(grid);

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                String categoryName = categoryNameField.getText();
                double goal;

                try {
                    goal = Double.parseDouble(goalField.getText());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid goal value: " + goalField.getText());
                    return null;
                }

                boolean isDuplicate = categories.getCategories().stream()
                        .anyMatch(category -> category.getName().equalsIgnoreCase(categoryName));
                if (isDuplicate) {
                    System.err.println("Category name already exists: " + categoryName);
                    return null;
                }

                int ID = nextID++;
                return new Category(ID, categoryName, 0, goal);
            }

            return null;
        });

        return dialog;
    }

    /**
     * Creates grid pane for dialog
     * @param categoryNameField name field
     * @param goalField goal field
     * @return grid pane
     */
    private GridPane createCategoryDialogGrid(TextField categoryNameField, TextField goalField) {
        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Category Name:"), categoryNameField);
        grid.addRow(1, new Label("Goal:"), goalField);
        return grid;
    }

    @Override
    public Node getView() {
        return view.getNode();
    }

    /**
     * Function that returns recent transaction
     * @param numRecentTransactions number of transaction to retrieve
     * @return transaction list
     */
    public List<Transaction> getRecentTransactions(int numRecentTransactions) {
        List<Transaction> allTransactions = transactions.getTransactions();
        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        List<Transaction> recentTransactions = new ArrayList<>();

        for (int i = 0; i < numRecentTransactions && i < allTransactions.size(); i++){
            recentTransactions.add(allTransactions.get(i));
        }
        return recentTransactions;
    }

    /**
     * Function that handles editing category
     * @param category
     */
    public void handleEditCategory(Category category) {
        Dialog<Category> dialog = createEditCategoryDialog(category);
        Optional<Category> result = dialog.showAndWait();
        if(result.isPresent()){
            Category editedCategory = result.get();

            // Update the current and goal properties with the new values
            category.setName(editedCategory.getName());
            category.setCurrent(editedCategory.getCurrent());
            category.setGoal(editedCategory.getGoal());

            view.refreshCategoryList();

            storageManager.updateCategoryFile();
        }
    }

    /**
     * Helper function that creates dialog for editing category
     * @param category instance to edit
     * @return dialog
     */
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

    /**
     * Function that handles category deletion
     * @param category instance to delete
     */
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

    /**
     * Calculates current for current month
     */
    public void countCurrent() {
        storageManager.calculateCurrentForCurrentMonth();
    }
}
