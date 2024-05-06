package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferForm extends Application {

    private TextField cardNumberTextField = new TextField();


    public static String receiverCardNumber = "";

    public static String receiverName = "";

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

        gridPane.add(new Label("Card Number:"), 0, 1);
        gridPane.add(cardNumberTextField, 1, 1);

        Button getNameButton = new Button("Next");
        getNameButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        gridPane.add(getNameButton, 1, 2);
        getNameButton.setDefaultButton(true);

        Button CancelButton = new Button("Cancel");
        CancelButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        gridPane.add(CancelButton, 2, 2);

        CancelButton.setOnAction(e -> {

            primaryStage.close();


        });
        getNameButton.setOnAction(e -> {
            try {
                receiverName = getName(cardNumberTextField.getText());
                receiverCardNumber = cardNumberTextField.getText();
                if (receiverName.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Card number not found");
                    alert.setContentText("Please enter a valid card number");
                    alert.showAndWait();
                } else {
                    new TransferForm2().start(primaryStage);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        Scene scene = new Scene(gridPane, 300, 100);
        primaryStage.setTitle("Transfer Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public String getName(String cardNumber) throws SQLException {
        String name = "";
        String query = "SELECT name FROM cards WHERE cardNumber = " + cardNumber;
        ResultSet rs = DatabaseConnection.getData(query);
        while (rs.next()) {
            name = rs.getString("name");
        }
        return name;
    }

}
