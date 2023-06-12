package com.budget.expansetracker.controllers;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.ReportView;
import javafx.scene.Node;

public class ReportController implements IController {

    private ReportView view;

    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    public ReportController(CategoryModel categoryModel, TransactionModel transactionModel){
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
        view = new ReportView(this, categoryModel, transactionModel);
    }
    @Override
    public Node getView() {
        return null;
    }
}
