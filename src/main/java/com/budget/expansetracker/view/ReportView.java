package com.budget.expansetracker.view;

import com.budget.expansetracker.Category;
import com.budget.expansetracker.Transaction;
import com.budget.expansetracker.controllers.ReportController;
import com.budget.expansetracker.model.CategoryModel;
import com.budget.expansetracker.model.TransactionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
        createMonthlyTrendsChart();
        // Create the pie chart and configure it
        PieChart pieChart = createPieChart();
        root.getChildren().add(pieChart);
    }

    private PieChart createPieChart(){
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
        return new PieChart(pieChartData);
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

    private void createMonthlyTrendsChart() {
        // Retrieve transactions
        List<Transaction> transactions = transactionModel.getTransactions();

        // Group transactions by month
        Map<YearMonth, List<Transaction>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> YearMonth.from(transaction.getDate())));

        // Calculate total expenses for each month
        Map<YearMonth, Double> monthlyExpenses = new TreeMap<>();
        for (Map.Entry<YearMonth, List<Transaction>> entry : transactionsByMonth.entrySet()) {
            YearMonth month = entry.getKey();
            List<Transaction> monthTransactions = entry.getValue();
            double totalExpense = monthTransactions.stream()
                    .filter(transaction -> transaction.getType() == Transaction.TransactionType.EXPENSE)
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            monthlyExpenses.put(month, totalExpense);
        }

        // Create a line chart
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

        // Set chart properties
        lineChart.setTitle("Monthly Expense Trends");
        lineChart.getXAxis().setLabel("Month");
        lineChart.getYAxis().setLabel("Total Expense");

        // Create series for data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        // Add data to the series
        for (Map.Entry<YearMonth, Double> entry : monthlyExpenses.entrySet()) {
            YearMonth month = entry.getKey();
            double totalExpense = entry.getValue();
            series.getData().add(new XYChart.Data<>(month.toString(), totalExpense));
        }

        // Add the series to the line chart
        lineChart.getData().add(series);

        // Customize other chart properties as desired

        // Add the line chart to the root container
        root.getChildren().add(lineChart);
    }


}
