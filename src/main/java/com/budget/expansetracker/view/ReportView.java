package com.budget.expansetracker.view;

import com.budget.expansetracker.controllers.ReportController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.scene.Node;

public class ReportView implements IView {

    private ReportController reportController;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;


    public ReportView(ReportController reportController, CategoryModel categoryModel, TransactionModel transactionModel){
        this.reportController = reportController;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
    }
    @Override
    public Node getNode() {
        return null;
    }
}
