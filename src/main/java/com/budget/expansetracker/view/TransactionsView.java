package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TransactionsView implements IView{

    private TransactionsController controller;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    private VBox root;
    private Button addTransactionButton;

    public TransactionsView(TransactionsController controller, CategoryModel categoryModel, TransactionModel transactionModel){
        this.controller = controller;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
        createView();
    }
    @Override
    public Node getNode() {
        return root;
    }

    private void createView(){
        root = new VBox();
        addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(controller::handleAddTransactionButton);

        TableView<Transaction> transactionTableView = new TableView<>();

        //TableColumn<Transaction, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Transaction, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        TableColumn<Transaction, Transaction.TransactionType> typeColumn = new TableColumn<>("Type");
        TableColumn<Transaction, Category> categoryColumn = new TableColumn<>("Category");
        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Description");

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(cellData ->  new SimpleObjectProperty<>(cellData.getValue().getType()));
        categoryColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            Category category = transaction.getCategory();
            if (category == null) {
                return new SimpleObjectProperty<>(categoryModel.getDefaultCategory());
            } else {
                return new SimpleObjectProperty<>(category);
            }
        });
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        nameColumn.setOnEditCommit(event -> {
            // Get the edited value
            String editedName = event.getNewValue();

            // Get the transaction being edited
            Transaction editedTransaction = event.getRowValue();

            // Update the transaction with the edited name
            editedTransaction.setName(editedName);
        });

        nameColumn.setEditable(true);
        transactionTableView.setEditable(true);

        categoryColumn.setPrefWidth(120);
        descriptionColumn.setPrefWidth(200);

        transactionTableView.getColumns().addAll(nameColumn, dateColumn, amountColumn, typeColumn, categoryColumn, descriptionColumn);

        transactionTableView.setItems(transactionModel.getTransactions());

        root.getChildren().addAll(addTransactionButton, transactionTableView);
    }
}
