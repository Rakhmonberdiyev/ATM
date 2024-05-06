package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.SQLException;

public class WithdrawForm extends Application {

    private double card_balance = CheckBalanceForm.getBalance();
    private Label balance_label;
    private TextField withdrawal_field;
    private Label message_label;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a label to display the card balance
        balance_label = new Label("$" + card_balance);
        balance_label.setStyle("-fx-font-size: 24px;");

        // Create a text field for custom withdrawal amount with smaller font size and shorter width
        withdrawal_field = new TextField();
        withdrawal_field.setPromptText("Amount");
        withdrawal_field.setStyle("-fx-font-size: 14px;");
        withdrawal_field.setPrefWidth(80); // Set preferred width

        // Create buttons for standard withdrawal amounts with smaller font size
        Button withdraw50Button = create_withdrawal_button(50.00);
        Button withdraw100Button = create_withdrawal_button(100.00);
        Button withdraw150Button = create_withdrawal_button(150.00);
        Button withdraw200Button = create_withdrawal_button(200.00);


        Button withdrawCustomButton = new Button("Withdraw");
        withdrawCustomButton.setStyle("-fx-font-size: 14px;");
        withdrawCustomButton.setOnAction(event -> withdraw_custom_amount());

        Button BackButton = new Button("Back");
        BackButton.setStyle("-fx-font-size: 14px;");
        BackButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        BackButton.setOnAction(event ->
                primaryStage.close()
        );


        message_label = new Label("");
        message_label.setTextFill(Color.GREEN);
        Label instructionLabel = new Label("Select or enter a withdrawal amount:");
        instructionLabel.setStyle("-fx-font-size: 16px;");

        // Create an HBox to hold the standard withdrawal buttons
        HBox standardButtonsBox = new HBox(10);
        standardButtonsBox.setAlignment(Pos.CENTER);
        standardButtonsBox.getChildren().addAll(withdraw50Button, withdraw100Button, withdraw150Button, withdraw200Button);

        // Create a VBox to organize the components vertically
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(balance_label, instructionLabel, standardButtonsBox, withdrawal_field, withdrawCustomButton, message_label);

        HBox customButtonsBox = new HBox(10);
        customButtonsBox.setAlignment(Pos.CENTER);
        customButtonsBox.getChildren().addAll(withdrawCustomButton, BackButton);
        mainBox.getChildren().add(customButtonsBox);

        //PUT the BackButton after the withdrawCustomButton

        mainBox.getChildren().add(BackButton);
        // Create a layout pane (StackPane) to center the VBox
        StackPane root = new StackPane();
        root.getChildren().add(mainBox);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);");

        // Set up the scene
        Scene scene = new Scene(root, 400, 300);

        // Set up the stage
        primaryStage.setTitle("Enhanced ATM Withdrawal Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button create_withdrawal_button(double amount) {
        Button button = new Button("$" + amount);
        button.setStyle("-fx-font-size: 14px;");
        button.setOnAction(event -> withdraw_amount(amount));
        return button;
    }

    private void withdraw_custom_amount() {
        String input = withdrawal_field.getText();
        try { double custom_amount = Double.parseDouble(input);
            withdraw_amount(custom_amount);
        } catch (NumberFormatException e) { setMessage("Invalid amount. Please enter a valid number");
        }}

    private void withdraw_amount(double amount) {
        if (amount <= 0) { setMessage("Please enter a valid positive amount.");
        } else if (card_balance >= amount) {
            card_balance -= amount;
            updateBalanceLabel();
            updateBalanceInDatabase();
            setMessage ("Withdrawal Successful: $" + amount);}
        else {
            setMessage ("Insufficient Funds!");}}

    private void updateBalanceLabel() {
        balance_label.setText("$" + card_balance);}
    private void setMessage(String message){
        message_label.setText(message);}

    private void updateBalanceInDatabase() {
        try {
            BigInteger cardNumber = LoginForm.getCardNumber();
            String sqlCommand = "UPDATE `cards` SET `balance` = " + card_balance + " WHERE `cardNumber` = " + cardNumber;
            DatabaseConnection.updateData(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}