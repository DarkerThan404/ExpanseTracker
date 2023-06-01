package com.budget.expansetracker.controllers;

import com.budget.expansetracker.view.OverviewView;
import javafx.scene.Node;

public class OverviewController implements IController {
    private OverviewView view;

    @Override
    public Node getView() {
        if (view == null){
            view = new OverviewView();
        }
        return view.getNode();
    }
}
