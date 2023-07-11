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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ReportView implements IView {

    private ReportController reportController;
    private CategoryModel categoryModel;
    private TransactionModel transactionModel;

    private HBox root;

    public ReportView(ReportController reportController, CategoryModel categoryModel, TransactionModel transactionModel){
        this.reportController = reportController;
        this.categoryModel = categoryModel;
        this.transactionModel = transactionModel;

    }
    @Override
    public Node getNode() {
        createView();
        return root;
    }

    private void createView(){
        root = new HBox();
        LineChart lineChart = createMonthlyTrendsChart();
        BorderPane borderPane = createExpenseComparisonChart();
        BorderPane pieChart = createPieChartView();

        VBox graphContainer = new VBox();
        graphContainer.getChildren().add(lineChart);
        graphContainer.getChildren().add(borderPane);

        root.getChildren().add(graphContainer);
        root.getChildren().add(pieChart);
    }

    private BorderPane createPieChartView() {

        Label timeLabel = new Label("Time period");
        // Create ComboBox for selecting month
        ComboBox<String> monthComboBox = new ComboBox<>();
        List<String> options = new ArrayList<>();
        options.add("All");
        for (Month month : Month.values()) {
            options.add(month.toString());
        }
        monthComboBox.setValue("All");
        monthComboBox.setItems(FXCollections.observableArrayList(options));
        HBox timeContainer = new HBox();
        timeContainer.getChildren().addAll(timeLabel, monthComboBox);

        // Create the PieChart
        PieChart pieChart = createPieChart();

        // Set up event listener for ComboBox
        monthComboBox.setOnAction(event -> {
            String selectedMonth = monthComboBox.getValue();
            if (selectedMonth.equals("All")) {
                // Show all transactions
                updatePieChartData(pieChart , getCategoryAmounts(selectedMonth));
            } else {
                // Filter transactions based on selected month
                updatePieChartData(pieChart, getCategoryAmounts(selectedMonth));
            }
        });

        // Create a container to hold the ComboBox and PieChart
        BorderPane container = new BorderPane();
        container.setTop(timeContainer);
        container.setCenter(pieChart);

        return container;
    }

    private PieChart createPieChart(){
        // Retrieve the amounts for each category
        Map<Category, Double> categoryAmounts = getCategoryAmounts("All");

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


    private void updatePieChartData(PieChart pieChart, Map<Category, Double> categoryAmounts) {
        // Clear existing data

        pieChart.getData().clear();

        // Populate the pie chart data with the updated categories and amounts
        for (Map.Entry<Category, Double> entry : categoryAmounts.entrySet()) {
            Category category = entry.getKey();
            Double amount = entry.getValue();
            PieChart.Data data = new PieChart.Data(category.getName(), amount);
            pieChart.getData().add(data);
        }
    }

    // Retrieve the amounts for each category from the transaction model
    private Map<Category, Double> getCategoryAmounts(String month) {
        Map<Category, Double> categoryAmounts = new HashMap<>();

        List<Transaction> transactions = transactionModel.getTransactions().stream()
                .filter(transaction -> (transaction.getDate().getMonth().toString().equalsIgnoreCase(month) || month.equalsIgnoreCase("All"))
                        && transaction.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.toList());

        for (Transaction transaction : transactions) {
            Category category = transaction.getCategory();
            double amount = transaction.getAmount();
            // Add the amount to the existing total for the category
            categoryAmounts.merge(category, amount, Double::sum);
        }
        return categoryAmounts;
    }

    private LineChart<String, Number> createMonthlyTrendsChart() {
        // Retrieve transactions
        List<Transaction> transactions = transactionModel.getTransactions();

        // Group transactions by month
        Map<YearMonth, List<Transaction>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> YearMonth.from(transaction.getDate())));

        // Calculate total expenses and income for each month
        Map<YearMonth, Double> monthlyExpenses = new TreeMap<>();
        Map<YearMonth, Double> monthlyIncome = new TreeMap<>();

        for (Map.Entry<YearMonth, List<Transaction>> entry : transactionsByMonth.entrySet()) {
            YearMonth month = entry.getKey();
            List<Transaction> monthTransactions = entry.getValue();

            double totalExpense = monthTransactions.stream()
                    .filter(transaction -> transaction.getType() == Transaction.TransactionType.EXPENSE)
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            monthlyExpenses.put(month, totalExpense);

            double totalIncome = monthTransactions.stream()
                    .filter(transaction -> transaction.getType() == Transaction.TransactionType.INCOME)
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            monthlyIncome.put(month, totalIncome);
        }

        // Create a line chart
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

        // Set chart properties
        lineChart.setTitle("Monthly Expense Trends");
        lineChart.getXAxis().setLabel("Month");
        lineChart.getYAxis().setLabel("Total Amount");

        // Create series for expenses
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        // Add expense data to the series
        for (Map.Entry<YearMonth, Double> entry : monthlyExpenses.entrySet()) {
            YearMonth month = entry.getKey();
            double totalExpense = entry.getValue();
            expenseSeries.getData().add(new XYChart.Data<>(month.toString(), totalExpense));
        }

        // Create series for income
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        // Add income data to the series
        for (Map.Entry<YearMonth, Double> entry : monthlyIncome.entrySet()) {
            YearMonth month = entry.getKey();
            double totalIncome = entry.getValue();
            incomeSeries.getData().add(new XYChart.Data<>(month.toString(), totalIncome));
        }

        // Add the series to the line chart
        lineChart.getData().addAll(expenseSeries, incomeSeries);

        // Customize other chart properties as desired

        // Add the line chart to the root container
        return lineChart;
    }



    private BorderPane createExpenseComparisonChart() {
        ComboBox<Month> month1ComboBox = new ComboBox<>();
        ComboBox<Month> month2ComboBox = new ComboBox<>();
        ComboBox<String> categoryComboBox = new ComboBox<>();

        month1ComboBox.getItems().addAll(Month.values());
        month2ComboBox.getItems().addAll(Month.values());
        categoryComboBox.getItems().add("All");
        categoryModel.getCategories().forEach(category -> categoryComboBox.getItems().add(category.getName()));
        categoryComboBox.getItems().add(categoryModel.getDefaultCategory().getName());

        month1ComboBox.setValue(Month.JUNE);
        month2ComboBox.setValue(Month.MAY);
        categoryComboBox.setValue("All");

        // Create a BorderPane to hold the ComboBoxes and the BarChart
        BorderPane chartContainer = new BorderPane();
        chartContainer.setTop(new HBox(10, categoryComboBox, month1ComboBox, month2ComboBox));

        // Retrieve transactions
        List<Transaction> transactions = transactionModel.getTransactions();

        // Update the chart when the ComboBox selections change
        month1ComboBox.setOnAction(event -> updateExpenseComparisonChart(chartContainer, transactions));
        month2ComboBox.setOnAction(event -> updateExpenseComparisonChart(chartContainer, transactions));
        categoryComboBox.setOnAction(event -> updateExpenseComparisonChart(chartContainer, transactions));

        // Initialize the chart with default values
        updateExpenseComparisonChart(chartContainer, transactions);

        return chartContainer;
    }

    private void updateExpenseComparisonChart(BorderPane chartContainer, List<Transaction> transactions) {
        ComboBox<Month> month1ComboBox = (ComboBox<Month>) ((HBox) chartContainer.getTop()).getChildren().get(1);
        ComboBox<Month> month2ComboBox = (ComboBox<Month>) ((HBox) chartContainer.getTop()).getChildren().get(2);
        ComboBox<String> categoryComboBox = (ComboBox<String>) ((HBox) chartContainer.getTop()).getChildren().get(0);

        Month month1 = month1ComboBox.getValue();
        Month month2 = month2ComboBox.getValue();
        String selectedCategory = categoryComboBox.getValue();

        // Filter transactions based on months and selected category
        List<Transaction> month1Transactions = transactions.stream()
                .filter(transaction -> transaction.getDate().getMonth() == month1)
                .filter(transaction -> selectedCategory.equals("All") || transaction.getCategory().getName().equals(selectedCategory))
                .collect(Collectors.toList());
        List<Transaction> month2Transactions = transactions.stream()
                .filter(transaction -> transaction.getDate().getMonth() == month2)
                .filter(transaction -> selectedCategory.equals("All") || transaction.getCategory().getName().equals(selectedCategory))
                .collect(Collectors.toList());

        // Create a stacked bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

        // Set chart properties
        stackedBarChart.setTitle("Expense Comparison");
        xAxis.setLabel("Categories");
        yAxis.setLabel("Total Expense");
        // Get the selected categories
        List<String> selectedCategories;
        if (selectedCategory.equals("All")) {
            // If "All" is selected, include all categories
            selectedCategories = categoryModel.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toList());
            selectedCategories.add(categoryModel.getDefaultCategory().getName());
        } else {
            // Include only the selected category
            selectedCategories = Collections.singletonList(selectedCategory);
        }

        // Iterate over categories
        for (String category : selectedCategories) {
            // Create a series for the category
            XYChart.Series<String, Number> categorySeries = new XYChart.Series<>();
            categorySeries.setName(category);

            double month1expense = calculateCategoryExpense(month1Transactions, category);
            double month2expense = calculateCategoryExpense(month2Transactions, category);

            categorySeries.getData().add(new XYChart.Data<>(month1.toString(), month1expense));
            // Add data for month2 only if it's different from month1
            if (!month2.equals(month1)) {
                categorySeries.getData().add(new XYChart.Data<>(month2.toString(), month2expense));
            }

                // Add the category series to the chart
            stackedBarChart.getData().add(categorySeries);
        }

        // Set the stacked bar chart in the center of the BorderPane
        chartContainer.setCenter(stackedBarChart);
    }

    private double calculateCategoryExpense(List<Transaction> transactions, String category) {
        double totalExpense = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getCategory().getName().equals(category) && transaction.getType().equals(Transaction.TransactionType.EXPENSE)) {
                totalExpense += transaction.getAmount();
            }
        }

        return totalExpense;
    }


}
