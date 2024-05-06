package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.TransferForm.receiverCardNumber;

public class TransferForm2 extends Application {

    private TextField transferAmountTextField = new TextField();
    private TextField destinationCardNumberTextField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);");

        gridPane.add(new Label("You are transferring to:"), 0, 1);
        gridPane.add(new Label(TransferForm.receiverName), 1, 1);

        gridPane.add(new Label("Your current balance is:"), 0, 0);
        gridPane.add(new Label(String.valueOf(CheckBalanceForm.getBalance()) +'$'), 1, 0);

        gridPane.add(new Label("Transfer Amount:"), 0, 2);
        gridPane.add(transferAmountTextField, 1, 2);



        Button transferButton = new Button("Transfer");
        transferButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        gridPane.add(transferButton, 2, 2);
        transferButton.setDefaultButton(true);

        Button CancelButton = new Button("Cancel");
        CancelButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
//        end of the grid  pane ADD button
        gridPane.add(CancelButton, 2, 4);

        Button BackButton = new Button("Back");
        BackButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        gridPane.add(BackButton, 1, 4);


        transferButton.setOnAction(e -> {
            try {

                transferFunds();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        CancelButton.setOnAction(e -> {

                primaryStage.close();


        });
        BackButton.setOnAction(e -> {
            BackTransfer();

        });

        Scene scene = new Scene(gridPane, 370, 160);
        primaryStage.setTitle("Transfer Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void BackTransfer(){
        TransferForm transfer = new TransferForm();
        transfer.start(new Stage());
        System.out.println("Showing Transfer form");
        Stage stage = (Stage) transferAmountTextField.getScene().getWindow();
        stage.close();
    }


    private void transferFunds() throws SQLException {
        String transferAmount = transferAmountTextField.getText();
        String destinationCardNumber = receiverCardNumber;

        if (transferAmount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Transfer amount not entered");
            alert.setContentText("Please enter a valid transfer amount");
            alert.showAndWait();
        } else if (destinationCardNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Destination card number not entered");
            alert.setContentText("Please enter a valid destination card number");
            alert.showAndWait();
        } else {
            int transferAmountInt = Integer.parseInt(transferAmount);
            int balance = CheckBalanceForm.getBalance();
            if (transferAmountInt > balance) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Insufficient funds");
                alert.setContentText("Please enter a valid transfer amount");
                alert.showAndWait();
            } else {
                int newBalance = balance - transferAmountInt;
                String sqlCommand = "UPDATE `cards` SET `balance` = " + newBalance + " WHERE `cardNumber` = " + LoginForm.getCardNumber();
                DatabaseConnection.updateData(sqlCommand);
                sqlCommand = "SELECT balance FROM `cards` WHERE `cardNumber` = " + destinationCardNumber;
                int destinationBalance = 0;
                ResultSet resultSet = DatabaseConnection.getData(sqlCommand);
                if (resultSet.next()) {
                    destinationBalance = resultSet.getInt("balance");
                }
                int newDestinationBalance = destinationBalance + transferAmountInt;
                sqlCommand = "UPDATE `cards` SET `balance` = " + newDestinationBalance + " WHERE `cardNumber` = " + destinationCardNumber;
                DatabaseConnection.updateData(sqlCommand);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Transfer successful");

                alert.setContentText("You have successfully transferred " + transferAmount + " to " + TransferForm.receiverName);
                alert.showAndWait();
            }
        }

    }
}
