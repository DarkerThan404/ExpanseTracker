package com.budget.expansetracker.view;

import com.budget.expansetracker.controllers.TransactionsController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.scene.Node;

public class TransactionsView implements IView{

    private TransactionsController controller;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    public TransactionsView(TransactionsController controller, CategoryModel categoryModel, TransactionModel transactionModel){
        this.controller = controller;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
    }
    @Override
    public Node getNode() {
        return null;
    }
}
