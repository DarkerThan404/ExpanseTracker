package com.budget.expansetracker.controllers;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.TransactionsView;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionsController implements IController {

    private final TransactionsView view;

    private final DataStorageManager storageManager;

    private final CategoryModel categories;
    private final TransactionModel transactions;

    private int newID;

    public TransactionsController(DataStorageManager storageManager){
        this.storageManager = storageManager;
        categories = storageManager.getCategoryModel();
        transactions = storageManager.getTransactionModel();
        view = new TransactionsView(this, categories, transactions);
    }
    @Override
    public Node getView() {
        return view.getNode();
    }

    /**
     * Function that handles add transaction operation
     * @param event
     */
    public void handleAddTransactionButton(ActionEvent event) {
        Dialog<Transaction> dialog = createTransactionDialog();
        Optional<Transaction> result = dialog.showAndWait();

        result.ifPresent(transactions::add);
    }

    /**
     * Helper function to create dialog for creation of transactions
     * @return Dialog that is responsible for creating transaction
     */
    private Dialog<Transaction> createTransactionDialog(){

        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Fill in transaction details");


        TextField nameField = new TextField();
        DatePicker dateField = new DatePicker();
        TextField amountField = new TextField();
        ComboBox<Transaction.TransactionType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Transaction.TransactionType.INCOME, Transaction.TransactionType.EXPENSE);

        ComboBox<Category> categoryComboBox = new ComboBox<>();

        // Get the observable list of items from the combo box
        ObservableList<Category> categoryItems = categoryComboBox.getItems();

        // Add the default category to the beginning of the list
        categoryItems.add(categories.getDefaultCategory());
        categoryItems.addAll(categories.getCategories());

        // Set the modified list as the combo box's items
        categoryComboBox.setItems(categoryItems);

        TextField descriptionField = new TextField();

        Label nameLabelError = new Label();
        Label dateLabelError = new Label();
        Label amountLabelError = new Label();
        Label typeLabelError = new Label();
        Label categoryLabelError = new Label();

        nameLabelError.setTextFill(Color.RED);
        dateLabelError.setTextFill(Color.RED);
        amountLabelError.setTextFill(Color.RED);
        typeLabelError.setTextFill(Color.RED);
        categoryLabelError.setTextFill(Color.RED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        int rowIdx = 0;

        grid.addRow(rowIdx++, new Label("Name:"), nameField);
        grid.addRow(rowIdx++, nameLabelError);


        grid.addRow(rowIdx++, new Label("Date:"), dateField);
        grid.addRow(rowIdx++, dateLabelError);

        HBox amountTypeBox = new HBox(10);
        amountTypeBox.getChildren().addAll(new Label("Amount:"), amountField, new Label("Type:"), typeComboBox);
        grid.addRow(rowIdx++, amountTypeBox);

        grid.addRow(rowIdx++, amountLabelError, typeLabelError);

        grid.addRow(rowIdx++, new Label("Category:"), categoryComboBox);
        grid.addRow(rowIdx++, categoryLabelError);

        grid.addRow(rowIdx++, new Label("Description:"), descriptionField);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        // Disable the "Add" button by default
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Add a change listener to enable/disable the "Add" button
        ChangeListener<Object> fieldsChangeListener = (observable, oldValue, newValue) -> {
            boolean isNameValid = !nameField.getText().trim().isEmpty();
            boolean isDateValid = dateField.getValue() != null;
            boolean isAmountValid = amountField.getText().matches("^\\d*\\.?\\d+$");
            boolean isTypeValid = typeComboBox.getValue() != null;
            boolean isCategoryValid = categoryComboBox.getValue() != null;

            addButton.setDisable(!isNameValid || !isDateValid || !isAmountValid || !isTypeValid || !isCategoryValid);

            nameLabelError.setText(isNameValid ? "" : "Name is required");
            dateLabelError.setText(isDateValid ? "" : "Date is required");
            amountLabelError.setText(isAmountValid ? "" : "Amount must be a positive number");
            typeLabelError.setText(isTypeValid ? "" : "Type is required");
            categoryLabelError.setText(isCategoryValid ? "" : "Category is required");
        };

        // Register the change listener for all relevant fields
        nameField.textProperty().addListener(fieldsChangeListener);
        dateField.valueProperty().addListener(fieldsChangeListener);
        amountField.textProperty().addListener(fieldsChangeListener);
        typeComboBox.valueProperty().addListener(fieldsChangeListener);
        categoryComboBox.valueProperty().addListener(fieldsChangeListener);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                LocalDate date = dateField.getValue();
                double amount = Double.parseDouble(amountField.getText());
                Transaction.TransactionType type = typeComboBox.getValue();
                Category category = categoryComboBox.getValue();
                String description = descriptionField.getText();

                return new Transaction(newID++, name, date, amount, type, category, description);
            }
            return null;
        });

        return dialog;
    }

    /**
     * Function that handles deletion of transactions
     */
    public void handleDeleteTransactions() {
        List<Transaction> selectedTransactions = transactions.getTransactions().stream()
                .filter(Transaction::isSelected)
                .collect(Collectors.toList());

        storageManager.removeTransactions(selectedTransactions);
    }

    public void handleEditTransactionButton(Transaction transaction) {
        Dialog<Transaction> dialog = createEditTransactionDialog(transaction);
        Optional<Transaction> result = dialog.showAndWait();
        if(result.isPresent()){
            Transaction editedTransaction = result.get();

            // Update the current and goal properties with the new values
            transaction.setName(editedTransaction.getName());
            transaction.setDate(editedTransaction.getDate());
            transaction.setAmount(editedTransaction.getAmount());
            transaction.setType(editedTransaction.getType());
            transaction.setCategory(editedTransaction.getCategory());
            transaction.setDescription(editedTransaction.getDescription());
            //TODO: propagate changes to a file
        }
    }

    private Dialog<Transaction> createEditTransactionDialog(Transaction transaction) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("Fill in transaction details");


        TextField nameField = new TextField(transaction.getName());
        DatePicker dateField = new DatePicker(transaction.getDate());
        TextField amountField = new TextField(String.valueOf(transaction.getAmount()));
        ComboBox<Transaction.TransactionType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Transaction.TransactionType.INCOME, Transaction.TransactionType.EXPENSE);
        typeComboBox.setValue(transaction.getType());

        ComboBox<Category> categoryComboBox = new ComboBox<>();

        // Get the observable list of items from the combo box
        ObservableList<Category> categoryItems = categoryComboBox.getItems();

        // Add the default category to the beginning of the list
        categoryItems.add(categories.getDefaultCategory());
        categoryItems.addAll(categories.getCategories());

        // Set the modified list as the combo box's items
        categoryComboBox.setItems(categoryItems);
        categoryComboBox.setValue(transaction.getCategory());

        TextField descriptionField = new TextField(transaction.getDescription());

        Label nameLabelError = new Label();
        Label dateLabelError = new Label();
        Label amountLabelError = new Label();
        Label typeLabelError = new Label();
        Label categoryLabelError = new Label();

        nameLabelError.setTextFill(Color.RED);
        dateLabelError.setTextFill(Color.RED);
        amountLabelError.setTextFill(Color.RED);
        typeLabelError.setTextFill(Color.RED);
        categoryLabelError.setTextFill(Color.RED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        int rowIdx = 0;

        grid.addRow(rowIdx++, new Label("Name:"), nameField);
        grid.addRow(rowIdx++, nameLabelError);


        grid.addRow(rowIdx++, new Label("Date:"), dateField);
        grid.addRow(rowIdx++, dateLabelError);

        HBox amountTypeBox = new HBox(10);
        amountTypeBox.getChildren().addAll(new Label("Amount:"), amountField, new Label("Type:"), typeComboBox);
        grid.addRow(rowIdx++, amountTypeBox);

        grid.addRow(rowIdx++, amountLabelError, typeLabelError);

        grid.addRow(rowIdx++, new Label("Category:"), categoryComboBox);
        grid.addRow(rowIdx++, categoryLabelError);

        grid.addRow(rowIdx++, new Label("Description:"), descriptionField);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Disable the "Add" button by default
        Button addButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        addButton.setDisable(true);

        // Add a change listener to enable/disable the "Add" button
        ChangeListener<Object> fieldsChangeListener = (observable, oldValue, newValue) -> {
            boolean isNameValid = !nameField.getText().trim().isEmpty();
            boolean isDateValid = dateField.getValue() != null;
            boolean isAmountValid = amountField.getText().matches("^\\d*\\.?\\d+$");
            boolean isTypeValid = typeComboBox.getValue() != null;
            boolean isCategoryValid = categoryComboBox.getValue() != null;

            addButton.setDisable(!isNameValid || !isDateValid || !isAmountValid || !isTypeValid || !isCategoryValid);

            nameLabelError.setText(isNameValid ? "" : "Name is required");
            dateLabelError.setText(isDateValid ? "" : "Date is required");
            amountLabelError.setText(isAmountValid ? "" : "Amount must be a positive number");
            typeLabelError.setText(isTypeValid ? "" : "Type is required");
            categoryLabelError.setText(isCategoryValid ? "" : "Category is required");
        };

        // Register the change listener for all relevant fields
        nameField.textProperty().addListener(fieldsChangeListener);
        dateField.valueProperty().addListener(fieldsChangeListener);
        amountField.textProperty().addListener(fieldsChangeListener);
        typeComboBox.valueProperty().addListener(fieldsChangeListener);
        categoryComboBox.valueProperty().addListener(fieldsChangeListener);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText();
                LocalDate date = dateField.getValue();
                double amount = Double.parseDouble(amountField.getText());
                Transaction.TransactionType type = typeComboBox.getValue();
                Category category = categoryComboBox.getValue();
                String description = descriptionField.getText();

                return new Transaction(newID++, name, date, amount, type, category, description);
            }
            return null;
        });

        return dialog;
    }
}
