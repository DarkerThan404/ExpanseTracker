package com.budget.expansetracker.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OverviewView {
    private BorderPane root;
    private Label balanceLabel;
    private Text balanceText;
    private VBox categoriesBox;
    private VBox transactionsBox;

    public OverviewView(){
        createView();
    }

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

        // Create and configure the categories component
        categoriesBox = new VBox();
        categoriesBox.setSpacing(5);
        Label categoriesLabel = new Label("Categories:");

        // Add category items to the categoriesBox
        for (int i = 1; i <= 5; i++) {
            String categoryName = "Category " + i;
            double progress = i * 0.2;
            VBox categoryBox = createCategoryBox(categoryName, progress);
            categoriesBox.getChildren().add(categoryBox);
        }

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

    private VBox createCategoryBox(String categoryName, double progress) {
        VBox categoryBox = new VBox();
        categoryBox.setSpacing(5);
        categoryBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(categoryName);
        ProgressBar progressBar = new ProgressBar(progress);

        Label progressLabel = new Label(String.format("%.0f / %.0f", progress * 100, 100.0));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(progressBar, progressLabel);

        categoryBox.getChildren().addAll(nameLabel, stackPane);

        return categoryBox;
    }

    public void setBalanceText(String balance) {
        balanceText.setText(balance);
    }

}
