package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

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

        //idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(cellData ->  new SimpleObjectProperty<>(cellData.getValue().getType()));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        categoryColumn.setPrefWidth(120);
        descriptionColumn.setPrefWidth(200);

        transactionTableView.getColumns().addAll(nameColumn, dateColumn, amountColumn, typeColumn, categoryColumn, descriptionColumn);
        transactionTableView.setItems(transactionModel.getTransactions());

        root.getChildren().addAll(addTransactionButton, transactionTableView);
    }
}
