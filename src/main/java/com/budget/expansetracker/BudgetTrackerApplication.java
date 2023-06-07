package com.budget.expansetracker;

import com.budget.expansetracker.controllers.OverviewController;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.controllers.ReportController;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetTrackerApplication extends Application {

    private BorderPane root;

    private ToggleGroup toggleGroup;

    private OverviewController overviewController;
    private TransactionsController transactionsController;
    private ReportController reportController;

    private ToggleButton overviewButton;
    private ToggleButton transactionsButton;
    private ToggleButton reportButton;

    @Override
    public void start(Stage stage) {

        toggleGroup = new ToggleGroup();

        overviewButton = new ToggleButton("Overview");
        transactionsButton = new ToggleButton("Transactions");
        reportButton = new ToggleButton("Report");

        // Add ToggleButtons to ToggleGroup
        overviewButton.setToggleGroup(toggleGroup);
        transactionsButton.setToggleGroup(toggleGroup);
        reportButton.setToggleGroup(toggleGroup);

        // Set button1 as initially selected
        overviewButton.setSelected(true);

        overviewController = new OverviewController();
        transactionsController = new TransactionsController();
        reportController = new ReportController();

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == overviewButton) {
                    Node overviewView = overviewController.getView();
                    setContent(overviewView);
                } else if (newValue == transactionsButton) {
                    Node transactionsView = transactionsController.getView();
                    setContent(transactionsView);
                } else if (newValue == reportButton) {
                    Node visualsView = reportController.getView();
                    setContent(visualsView);
                }
            }
        });

        root = new BorderPane();

        root.setLeft(createMenu());

        root.setCenter(overviewController.getView());
        // Create the scene with the root layout
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("/styles.css");

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
        menu.getChildren().addAll(overviewButton, transactionsButton, reportButton);
        return menu;
    }
}