package com.budget.expansetracker;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetTrackerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox menu = new VBox();

        Button overviewButton = new Button("Overview");
        Button transactionsButton = new Button("Transactions");
        Button visualsButton = new Button("Visuals");

        menu.getChildren().addAll(overviewButton, transactionsButton, visualsButton);

        BorderPane root = new BorderPane();
        root.setLeft(menu);
        //root.setCenter(contentArea);

        // Create the scene with the root layout
        Scene scene = new Scene(root, 800, 600);

        // Set the scene for the primary stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}