package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.ReportController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportView implements IView {

    private ReportController reportController;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    private VBox root;


    public ReportView(ReportController reportController, CategoryModel categoryModel, TransactionModel transactionModel){
        this.reportController = reportController;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;
        createView();
    }
    @Override
    public Node getNode() {
        return root;
    }

    private void createView(){
        root = new VBox();

        // Retrieve the amounts for each category
        Map<Category, Double> categoryAmounts = getCategoryAmounts();

        // Create an observable list to hold the pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Populate the pie chart data
        for (Map.Entry<Category, Double> entry : categoryAmounts.entrySet()) {
            Category category = entry.getKey();
            Double amount = entry.getValue();
            PieChart.Data data = new PieChart.Data(category.getName(), amount);
            pieChartData.add(data);
        }

        // Create the pie chart and configure it
        PieChart pieChart = new PieChart(pieChartData);
        root.getChildren().add(pieChart);
    }

    // Retrieve the amounts for each category from the transaction model
    private Map<Category, Double> getCategoryAmounts() {
        Map<Category, Double> categoryAmounts = new HashMap<>();

        List<Transaction> transactions = transactionModel.getTransactions();
        for (Transaction transaction : transactions) {
            Category category = transaction.getCategory();
            double amount = transaction.getAmount();

            // Add the amount to the existing total for the category
            categoryAmounts.merge(category, amount, Double::sum);
        }

        return categoryAmounts;
    }
}
