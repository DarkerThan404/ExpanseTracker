package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.controllers.OverviewController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
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
    private Text balanceText;
    private VBox categoriesBox;
    private VBox transactionsBox;

    private Button addCategoryButton;
    private OverviewController controller;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    private ObservableList<Category> categories;
    private ListView<Category> categoryListView;

    public OverviewView(OverviewController controller, CategoryModel categoryModel, TransactionModel transactionModel) {
        this.controller = controller;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
        createView();

    }
    @Override
    public Node getNode(){
        return root;
    }

    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Create and configure the balance component
        balanceLabel = new Label("Balance:");
        balanceText = new Text("$5000"); // Replace with your balance value
        HBox balanceBox = new HBox(balanceLabel, balanceText);
        balanceBox.setAlignment(Pos.CENTER);

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
                    VBox categoryBox = createCategoryBox(category);
                    setGraphic(categoryBox);
                }
            }
        });

        // Create and configure the categories component
        categoriesBox = new VBox();
        categoriesBox.setSpacing(5);
        Label categoriesLabel = new Label("Categories:");

        addCategoryButton = new Button("Add Category");
        addCategoryButton.setOnAction(event -> controller.handleAddCategoryButton(event));

        categoriesBox.getChildren().add(addCategoryButton);
        categoriesBox.getChildren().add(categoryListView);




        // Create and configure the recent transactions component
        transactionsBox = new VBox();
        transactionsBox.setSpacing(5);
        Label transactionsLabel = new Label("Recent Transactions:");
        // Add transaction items to the transactionsBox
        for (int i = 1; i <= 5; i++) {
            Label transactionLabel = new Label("Transaction " + i);
            transactionsBox.getChildren().add(transactionLabel);
        }

        // Add the components to the root VBox
        root.setTop(balanceBox);
        root.setLeft(categoriesBox);
        root.setRight(transactionsBox);
    }

    private VBox createCategoryBox(Category category) {
        VBox categoryBox = new VBox();
        categoryBox.setSpacing(5);
        categoryBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(category.getName());
        ProgressBar progressBar = new ProgressBar(category.getGoal());

        Label progressLabel = new Label(String.format("%.0f / %.0f", category.getGoal() * 100, 100.0));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(progressBar, progressLabel);

        categoryBox.getChildren().addAll(nameLabel, stackPane);

        return categoryBox;
    }
}
