package com.budget.expansetracker.controllers;

import com.budget.expansetracker.DataStorageManager;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.ReportView;
import javafx.scene.Node;

public class ReportController implements IController {

    private final ReportView view;

    private final DataStorageManager storageManager;

    private final CategoryModel categoryModel;
    private final TransactionModel transactionModel;

    public ReportController(DataStorageManager storageManager){
        this.storageManager = storageManager;
        categoryModel = storageManager.getCategoryModel();
        transactionModel = storageManager.getTransactionModel();
        view = new ReportView(this, categoryModel, transactionModel);
    }
    @Override
    public Node getView() {
        return view.getNode();
    }
}
