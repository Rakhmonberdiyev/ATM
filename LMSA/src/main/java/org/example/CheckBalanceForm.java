package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.GridPane;

public class CheckBalanceForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static int balance;

    @Override
    public void start(Stage primary_stage) {
        // Create a label to display the card balance
        Label balance_label = new Label("Your current balance is: $" + getBalance());
        balance_label.setFont(Font.font("Arial", 24));
        balance_label.setTextFill(Color.GREEN);

        StackPane root = new StackPane();
        root.getChildren().add(balance_label);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);"); // Light cyan background color

        Scene scene = new Scene(root, 400, 250);

        primary_stage.setTitle("ATM Card Balance");
        primary_stage.setScene(scene);
        primary_stage.show();

        Button close_button = new Button("Close");
        close_button.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white");
        close_button.setOnAction(e -> {
            primary_stage.close();
        });

        GridPane grid_pane = new GridPane();
        grid_pane.add(close_button, 2, 2);
        grid_pane.setVgap(50);
        grid_pane.setAlignment(Pos.CENTER);
        root.getChildren().add(grid_pane);
    }

    public static int getBalance() {
        try {
            BigInteger card_number = LoginForm.getCardNumber();
            String sql_command = "SELECT balance FROM `cards` WHERE `cardNumber` = " + card_number;
            ResultSet result_set = DatabaseConnection.getData(sql_command);
            if (result_set.next()) {
                balance = result_set.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public static int getBalanceByCardNumber(BigInteger card_number) {
        int card_balance = 0;
        try {
            String sql_command = "SELECT balance FROM `cards` WHERE `cardNumber` = " + card_number;
            ResultSet result_set = DatabaseConnection.getData(sql_command);
            if (result_set.next()) {
                card_balance = result_set.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card_balance;
    }
}
