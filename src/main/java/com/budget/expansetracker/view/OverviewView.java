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

import java.util.List;

public class OverviewView implements IView {
    private BorderPane root;

    private Label balanceLabel;
    private DoubleProperty balanceProperty;

    private VBox categoriesBox;
    private VBox transactionsBox;

    private Button addCategoryButton;
    private final OverviewController controller;
    private final CategoryModel categoryModel;
    private final TransactionModel transactionModel;

    private ListView<Category> categoryListView;

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

    /**
     * Function that creates content view
     */
    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        transactionModel.calculateInitialBalance();
        HBox balanceBox = createBalanceComponent();
        createCategoriesComponent();
        createTransactionsComponent();

        // Add the components to the root BorderPane
        root.setTop(balanceBox);
        root.setLeft(categoriesBox);
        root.setRight(transactionsBox);
    }

    /**
     * Function that creates balance box
     * @return balance box
     */
    private HBox createBalanceComponent() {
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
        return balanceBox;
    }

    /**
     * Creates category box
     */
    private void createCategoriesComponent() {

        controller.countCurrent();
        categoryListView = new ListView<>();
        categoryListView.setItems(categoryModel.getCategories());
        categoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);

                if (empty || category == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox categoryBox = createCategoryBox(category);
                    setGraphic(categoryBox);
                }
            }
        });

        categoriesBox = new VBox();
        categoriesBox.setSpacing(5);

        addCategoryButton = new Button("Add Category");
        addCategoryButton.setOnAction(controller::handleAddCategory);

        Label remainder = new Label("Categories current values reset to zero at the start of the month.");
        remainder.getStyleClass().add("remainder");

        categoriesBox.getChildren().addAll(addCategoryButton, categoryListView, remainder);
        categoriesBox.setPrefWidth(320);
    }

    /**
     * Creates recent transactions box
     */
    private void createTransactionsComponent() {
        transactionsBox = new VBox();
        transactionsBox.setSpacing(5);

        Label transactionsLabel = new Label("Recent Transactions:");
        int recentTransactionCount = 10;
        List<Transaction> recentTransactions = controller.getRecentTransactions(recentTransactionCount);
        ListView<Transaction> transactionsListView = createTransactionView(recentTransactions);

        transactionsBox.getChildren().addAll(transactionsLabel, transactionsListView);
        transactionsBox.setPrefWidth(320);
    }

    /**
     * Creates category box that is then rendered in category box in view
     * @param category Object that is view created around
     * @return Category box
     */
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

    /**
     * Helper function that update category list
     *
     */
    public void refreshCategoryList() {
        categoryListView.refresh();
    }

    /**
     * Function that creates recent transaction view.
     * @param recentTransactions transaction
     * @return
     */
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
                    setText( transaction.getDate() + " - " + transaction.getName() + " - " + transaction.getAmount());
                }
            }
        });
        return transactionsListView;
    }
}
