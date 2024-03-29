# Budget Planner Application

The Budget Planner Application is a simple budget tracking tool that allows users to manage their expenses and categories. It is built using Java and JavaFX and provides a graphical user interface (GUI) for easy interaction.

## Features

- Add, edit, and delete expense categories.
- Add, edit, and delete transactions with associated categories. (delete button triggers selection column where you select undesired rows and by confirm deletes them, or you cancel by pressing cancel button)
- View summary of expenses for each category.
- Calculate the current expenses for the current month.
- Store data in CSV files for persistence between sessions.
- Visualize proportional expenses as pie chart with customizable periods.
- Compare expenses across two different months.
- Visualize monthly trends for both expenses and income with line graphs.

## Installation

1. Unzip file
2. Open the project in your Java IDE (e.g., IntelliJ, Eclipse).

## Usage

1. Run the `BudgetTrackerApplication.java` class to start the application.
2. The application will create the necessary data files (`transactions.csv` and `categories.csv`) in the user's home directory under a subdirectory named `BudgetPlanner/data`(or you can copy files prepared for demo in data folder of main application).
3. Use the GUI to add, edit, and delete categories and transactions as needed.
4. The application will automatically save changes to the data files.

## Requirements

- Java 17
- JavaFX

## License

This project is licensed under the [MIT License](LICENSE).

Feel free to modify and use this application according to your needs.

## Author

- David Truhlář

## Acknowledgments

Special thanks to the creators of Java, JavaFX, and any libraries used in this project.
