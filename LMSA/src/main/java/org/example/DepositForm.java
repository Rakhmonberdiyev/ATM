package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.SQLException;

public class DepositForm extends Application {

    private double accountBalance = CheckBalanceForm.getBalance(); // Initial account balance for demonstration purposes

    private Label balanceLabel;
    private TextField amountField;
    private ChoiceBox<String> timeUnitChoice;
    private TextField durationField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Deposit Form");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #ADD8E6;"); // Light blue background color

        Label accountLabel = new Label("Account Balance:");
        balanceLabel = new Label(String.format("$ %.2f", accountBalance));

        Label amountLabel = new Label("Deposit Amount:");
        amountField = new TextField();

        Label timeUnitLabel = new Label("Time Unit:");
        timeUnitChoice = new ChoiceBox<>();
        timeUnitChoice.getItems().addAll("Months", "Years");
        timeUnitChoice.setValue("Months");

        Label durationLabel = new Label("Duration:");
        durationField = new TextField();

        Button depositButton = new Button("Deposit");
        depositButton.setOnAction(e -> handleDeposit());
        depositButton.setDefaultButton(true);
        depositButton.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white;"); // Dark blue background color

        // Add components to the grid layout
        grid.add(accountLabel, 0, 0);
        grid.add(balanceLabel, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(timeUnitLabel, 0, 2);
        grid.add(timeUnitChoice, 1, 2);
        grid.add(durationLabel, 0, 3);
        grid.add(durationField, 1, 3);
        grid.add(depositButton, 0, 4, 2, 1);

        // Create a scene and set it to the stage
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    // Method to handle the deposit action
    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                // Display an error for non-positive deposit amounts
                System.out.println("Invalid deposit amount. Please enter a positive value.");
                return;
            }

            int duration = Integer.parseInt(durationField.getText());
            if (duration <= 0) {
                // Display an error for non-positive durations
                System.out.println("Invalid duration. Please enter a positive value.");
                return;
            }

            String timeUnit = timeUnitChoice.getValue();
            System.out.println(timeUnit);


            double interestRate = 0.1; // Example: 10% annual interest rate
            if(timeUnit == "Months"){
                interestRate /= 13;
            }


            // Calculate future value using the compound interest formula
            double futureValue = amount * Math.pow(1 + interestRate, duration);

            // Update the account balance
            accountBalance += futureValue;

            updateBalanceInDatabase();

            // Display the updated balance
            System.out.println("Deposit successful. New balance: $" + accountBalance);

            // Update the UI (for demonstration purposes; in a real application, you might update the UI differently)
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            // Display an error for invalid numeric input
            System.out.println("Invalid input. Please enter valid numeric values.");
        }
    }

    // Method to update the balance label in the UI
    private void updateBalanceLabel() {
        // This is a simple demonstration; in a real application, you might use bindings or other UI update mechanisms
        balanceLabel.setText(String.format("$ %.2f", accountBalance));
    }
    private void updateBalanceInDatabase() {
        try {
            BigInteger cardNumber = LoginForm.getCardNumber();
            String sqlCommand = "UPDATE `cards` SET `balance` = " + accountBalance + " WHERE `cardNumber` = " + cardNumber;
            DatabaseConnection.updateData(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
