package com.budget.expansetracker.controllers;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.TransactionsView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Optional;

public class TransactionsController implements IController {

    private TransactionsView view;

    private DataStorageManager storageManager;

    private CategoryModel categories;
    private TransactionModel transactions;

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

    public void handleAddTransactionButton(ActionEvent event) {
        Dialog<Transaction> dialog = createTransactionDialog();
        Optional<Transaction> result = dialog.showAndWait();

        result.ifPresent(transaction -> {
            transactions.add(transaction);
        });
    }

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

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Date:"), dateField);

        HBox amountTypeBox = new HBox(10);
        amountTypeBox.getChildren().addAll(new Label("Amount:"), amountField, new Label("Type:"), typeComboBox);
        grid.addRow(2, amountTypeBox);

        grid.addRow(3, new Label("Category:"), categoryComboBox);
        grid.addRow(4, new Label("Description:"), descriptionField);
        dialog.getDialogPane().setContent(grid);

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
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
