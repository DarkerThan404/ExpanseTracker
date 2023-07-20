package com.budget.expansetracker;

import com.budget.expansetracker.controllers.OverviewController;
import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.controllers.ReportController;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BudgetTrackerApplication extends Application {

    private BorderPane root;

    private ToggleGroup toggleGroup;

    private DataStorageManager storageManager;

    private OverviewController overviewController;
    private TransactionsController transactionsController;
    private ReportController reportController;

    private ToggleButton overviewButton;
    private ToggleButton transactionsButton;
    private ToggleButton reportButton;

    /**
     * Entry function of application
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        toggleGroup = new ToggleGroup();

        overviewButton = new ToggleButton("Overview");
        transactionsButton = new ToggleButton("Transactions");
        reportButton = new ToggleButton("Report");

        overviewButton.setToggleGroup(toggleGroup);
        transactionsButton.setToggleGroup(toggleGroup);
        reportButton.setToggleGroup(toggleGroup);

        // Set overviewButton as initially selected
        overviewButton.setSelected(true);

        storageManager = new DataStorageManager();

        overviewController = new OverviewController(storageManager);
        transactionsController = new TransactionsController(storageManager);
        reportController = new ReportController(storageManager);

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

        Scene scene = new Scene(root, 960, 640);
        scene.getStylesheets().add("/styles.css");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets content for root
     * @param content instance to set
     */
    private void setContent(Node content){
        root.setCenter(content);
    }

    /**
     * Creates menu for main application
     * @return menu
     */
    private VBox createMenu(){
        VBox menu = new VBox();
        menu.getChildren().addAll(overviewButton, transactionsButton, reportButton);
        return menu;
    }
}