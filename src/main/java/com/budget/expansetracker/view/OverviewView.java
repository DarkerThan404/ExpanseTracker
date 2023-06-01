package com.budget.expansetracker.view;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class OverviewView {
    private VBox root;

    public OverviewView(){
        root = createView();
    }

    public Node getNode(){
        return root;
    }

    private VBox createView() {
        VBox vBox = new VBox();
        return vBox;
    }

}
