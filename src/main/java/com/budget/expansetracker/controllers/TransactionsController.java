package com.budget.expansetracker.controllers;

import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import com.budget.expansetracker.view.TransactionsView;
import javafx.scene.Node;

public class TransactionsController implements IController {

    private TransactionsView view;

    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    public TransactionsController(CategoryModel categoryModel, TransactionModel transactionModel){
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
        view = new TransactionsView(this, categoryModel, transactionModel);
    }
    @Override
    public Node getView() {
        return null;
    }
}
