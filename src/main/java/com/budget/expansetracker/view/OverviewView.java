package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.OverviewController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class OverviewView implements IView {
    private BorderPane root;

    private Label balanceLabel;
    private DoubleProperty balanceProperty;

    private VBox categoriesBox;
    private VBox transactionsBox;

    private Button addCategoryButton;
    private OverviewController controller;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    private ListView<Category> categoryListView;

    private final int recentTransactionCount = 5;

    public OverviewView(OverviewController controller, CategoryModel categoryModel, TransactionModel transactionModel) {
        this.controller = controller;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
    }
    @Override
    public Node getNode(){
        createView();
        return root;
    }

    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        transactionModel.calculateInitialBalance();
        // Create and configure the balance component
        balanceLabel = new Label("Balance:");
        balanceLabel.getStyleClass().add("balance-label");

        balanceProperty = new SimpleDoubleProperty();
        balanceLabel.textProperty().bind(balanceProperty.asString());

        double initialBalance = transactionModel.getBalance();
        balanceProperty.set(initialBalance);

        double epsilon = 0.001; // Choose an appropriate epsilon value for your application

        // Determine the initial balance value and apply the appropriate style class
        if (Math.abs(initialBalance) > epsilon) {
            if (initialBalance > 0) {
                balanceLabel.getStyleClass().add("balance-positive");
            } else {
                balanceLabel.getStyleClass().add("balance-negative");
            }
        } else {
            balanceLabel.getStyleClass().add("balance-zero");
        }

        HBox balanceBox = new HBox(balanceLabel);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.prefHeightProperty().bind(root.heightProperty().multiply(0.2));

        //categories = FXCollections.observableArrayList();
        categoryListView = new ListView<>();
        categoryListView.setItems(categoryModel.getCategories());
        categoryListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Category category, boolean empty){
                super.updateItem(category, empty);

                if( empty || category == null){
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox categoryBox = createCategoryBox(category);
                    setGraphic(categoryBox);
                }
            }
        });

        // Create and configure the categories component
        categoriesBox = new VBox();
        categoriesBox.setSpacing(5);

        addCategoryButton = new Button("Add Category");
        addCategoryButton.setOnAction(controller::handleAddCategoryButton);

        categoriesBox.getChildren().add(addCategoryButton);
        categoriesBox.getChildren().add(categoryListView);

        // Create and configure the recent transactions component
        transactionsBox = new VBox();
        transactionsBox.setSpacing(5);

        Label transactionsLabel = new Label("Recent Transactions:");
        List<Transaction> recentTransactions = controller.getRecentTransactions(recentTransactionCount);
        ListView<Transaction> transactionsListView = createTransactionView(recentTransactions);

        double desiredWidth = 320;

        categoriesBox.setPrefWidth(desiredWidth);
        transactionsBox.setPrefWidth(desiredWidth);


        transactionsBox.getChildren().add(transactionsLabel);
        transactionsBox.getChildren().add(transactionsListView);
        // Add the components to the root VBox
        root.setTop(balanceBox);
        root.setLeft(categoriesBox);
        root.setRight(transactionsBox);
    }

    private HBox createCategoryBox(Category category) {
        HBox categoryBox = new HBox();
        categoryBox.setSpacing(5);
        categoryBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(category.getName());
        ProgressBar progressBar = new ProgressBar();

        // Create properties for current and goal
        SimpleDoubleProperty currentProperty = new SimpleDoubleProperty(category.getCurrent());
        SimpleDoubleProperty goalProperty = new SimpleDoubleProperty(category.getGoal());

        progressBar.progressProperty().bind(
                Bindings.createDoubleBinding(() -> {
                    double current = category.getCurrent();
                    double goal = category.getGoal();
                    return current / goal;
                }, currentProperty, goalProperty)
        );

        double progress = category.getCurrent() / category.getGoal();

        if (progress > 1.0) {
            progressBar.getStyleClass().add("over-limit");
        } else if (progress < 1.0) {
            progressBar.getStyleClass().add("under-limit");
        } else {
            progressBar.getStyleClass().add("goal-met");
        }

        Label progressLabel = new Label(String.format("%.0f / %.0f", category.getCurrent(), category.getGoal()));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(progressBar, progressLabel);

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> controller.handleEditCategory(category));

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> controller.handleDeleteCategory(category));

        HBox buttonContainer = new HBox(editButton, deleteButton);
        buttonContainer.setSpacing(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        VBox headerContainer = new VBox(nameLabel, stackPane);
        headerContainer.setAlignment(Pos.CENTER);

        categoryBox.getChildren().addAll(headerContainer, buttonContainer);

        return categoryBox;
    }

    public void updateCategoryBoxInList(Category category) {
        ObservableList<Category> categories = categoryModel.getCategories();
        int index = categories.indexOf(category);
        if (index >= 0) {
            categoryListView.getItems().set(index, category);
        }
    }



    private ListView<Transaction> createTransactionView(List<Transaction> recentTransactions){

        ListView<Transaction> transactionsListView = new ListView<>();

        transactionsListView.setItems(FXCollections.observableArrayList(recentTransactions));
        transactionsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);

                if (empty || transaction == null) {
                    setText(null);
                } else {
                    Category transactionCategory = transaction.getCategory();
                    if(transactionCategory == null){
                        transactionCategory = categoryModel.getDefaultCategory();
                    }
                    setText(transaction.getName() + " - " + transaction.getAmount() + ", " + transaction.getDate() + ", " +  categoryModel.getCategoryByID(transactionCategory.getID()) );
                }
            }
        });
        return transactionsListView;
    }
}
