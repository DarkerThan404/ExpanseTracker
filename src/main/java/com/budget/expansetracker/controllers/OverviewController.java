package com.budget.expansetracker.controllers;

import com.budget.expansetracker.view.OverviewView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.util.Optional;

public class OverviewController implements IController {
    private OverviewView view;

    public OverviewController(){
        view = new OverviewView(this);
    }

    public void handleAddCategoryButton(ActionEvent event){
        // Create the dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter the category name:");

        // Set the dialog content
        TextField categoryNameField = new TextField();
        dialog.getDialogPane().setContent(categoryNameField);

        // Add buttons to the dialog
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);

        // Set the result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                return categoryNameField.getText();
            }
            return null;
        });

        // Show the dialog and handle the user's input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(categoryName -> {
            // Handle the category name
            // This code will be executed when the user clicks the "Add" button
            System.out.println("Category Name: " + categoryName);
        });
    }

    @Override
    public Node getView() {
        return view.getNode();
    }
}
