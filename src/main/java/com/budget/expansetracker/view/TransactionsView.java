package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsView implements IView{

    private final TransactionsController controller;
    private final CategoryModel categoryModel;
    private final TransactionModel transactionModel;

    private VBox root;
    private Button addTransactionButton;
    private Button deleteButton;
    private Button confirmDeleteButton;
    private Button cancelButton;
    private TableView<Transaction> transactionTableView;
    private HBox buttonContainer;
    private HBox confirmCancelContainer;

    private boolean deleteMode = false;

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

        createTableView();
        createButtons();
        initializeDeleteMode();

        root.getChildren().addAll(buttonContainer, transactionTableView, confirmCancelContainer);
    }

    private void createTableView(){
        transactionTableView = new TableView<>();

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

        transactionTableView.getColumns().add(nameColumn);
        transactionTableView.getColumns().add(dateColumn);
        transactionTableView.getColumns().add(amountColumn);
        transactionTableView.getColumns().add(typeColumn);
        transactionTableView.getColumns().add(categoryColumn);
        transactionTableView.getColumns().add(descriptionColumn);
        transactionTableView.setItems(transactionModel.getTransactions());
    }
    private void createButtons(){
        buttonContainer = new HBox(10);

        addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(controller::handleAddTransactionButton);

        deleteButton = new Button("Delete");

        buttonContainer.getChildren().addAll(addTransactionButton, deleteButton);

        confirmCancelContainer = new HBox(10);
        confirmCancelContainer.setAlignment(Pos.BOTTOM_RIGHT);

        confirmDeleteButton = new Button("Confirm Delete");
        cancelButton = new Button("Cancel");

        confirmDeleteButton.setOnAction(event -> {
            // Handle deletion logic here
            List<Transaction> selectedTransactions = transactionModel.getTransactions().stream()
                    .filter(Transaction::isSelected)
                    .collect(Collectors.toList());
            transactionModel.removeTranasactions(selectedTransactions);

            // Reset delete mode state
            deleteMode = false;
            deleteButton.setVisible(true);
            confirmDeleteButton.setVisible(false);
            confirmCancelContainer.getChildren().remove(transactionTableView);
        });
        //confirmDeleteButton.setVisible(false);

        confirmCancelContainer.getChildren().addAll(confirmDeleteButton, cancelButton);
    }

    private void initializeDeleteMode(){
        TableColumn<Transaction, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(param -> {
            Transaction transaction = param.getValue();
            BooleanProperty selected = new SimpleBooleanProperty(false);
            selected.addListener((obs, oldValue, newValue) -> transaction.setSelected(newValue));
            return selected;
        });
        selectColumn.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            if (deleteMode) {
                BooleanProperty selected = new SimpleBooleanProperty(
                        transactionTableView.getItems().get(index).isSelected());
                selected.addListener((obs, oldValue, newValue) ->
                        transactionTableView.getItems().get(index).setSelected(newValue));
                return selected;
            }
            return null;
        }));
        selectColumn.setEditable(true);
        deleteButton.setOnAction(event -> {
            deleteMode = !deleteMode;
            if (deleteMode) {
                deleteButton.setVisible(false);
                deleteButton.setManaged(false);
                confirmDeleteButton.setDisable(false);
                cancelButton.setDisable(false);
                if (!transactionTableView.getColumns().contains(selectColumn)) {
                    transactionTableView.getColumns().add(selectColumn);
                }
            } else {
                deleteButton.setVisible(true);
                deleteButton.setManaged(true);
                confirmDeleteButton.setDisable(true);
                cancelButton.setDisable(true);
                transactionTableView.getColumns().remove(selectColumn);
            }
        });
    }
}
