package com.budget.expansetracker;

import com.budget.expansetracker.controllers.OverviewController;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.controllers.VisualsController;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetTrackerApplication extends Application {

    private BorderPane root;

    private OverviewController overviewController;
    private TransactionsController transactionsController;
    private VisualsController visualsController;

    private Button overviewButton;
    private Button transactionsButton;
    private Button visualsButton;
    @Override
    public void start(Stage stage) throws IOException {

        overviewButton = new Button("Overview");
        transactionsButton = new Button("Transactions");
        visualsButton = new Button("Visuals");

        overviewController = new OverviewController();
        transactionsController = new TransactionsController();
        visualsController = new VisualsController();

        overviewButton.setOnAction( event -> {
            Node overviewView = overviewController.getView();
            setContent(overviewView);
        } );

        transactionsButton.setOnAction( event -> {
            Node transactionsView = transactionsController.getView();
            setContent(transactionsView);
        });

        visualsButton.setOnAction( event ->  {
            Node visualsView = visualsController.getView();
            setContent(visualsView);
        });


        root = new BorderPane();

        root.setLeft(createMenu());
        // Create the scene with the root layout
        Scene scene = new Scene(root, 800, 600);

        // Set the scene for the primary stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void setContent(Node content){
        root.setCenter(content);
    }

    private VBox createMenu(){
        VBox menu = new VBox();
        menu.getChildren().addAll(overviewButton, transactionsButton, visualsButton);
        return menu;
    }
}