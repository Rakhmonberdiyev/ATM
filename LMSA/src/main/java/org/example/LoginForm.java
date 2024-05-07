package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends Application {

    private TextField cardNumberTextField = new TextField();
    private PasswordField pincodeField = new PasswordField();

    private Button loginButton = new Button("Login");
    private Button cancelButton = new Button("Cancel");
    private Button createButton = new Button("Create Account");

    public static BigInteger cardNumber;

    public static BigInteger getCardNumber() {
        return cardNumber;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to ATM!");

        // Creating a GridPane with light blue background color
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);"); // Light blue background color

        // Add labels, text fields, checkbox, and buttons to the grid
        grid.add(new Label("Card number:"), 0, 0);
        grid.add(cardNumberTextField, 1, 0);

        grid.add(new Label("Pin code:"), 0, 1);
        grid.add(pincodeField, 1, 1);

        // Move the login button to the top line
        grid.add(loginButton, 0, 2);

        // Move the create button to the right of the login button
        grid.add(createButton, 1, 2);

        // Move the cancel button under login
        grid.add(cancelButton, 0, 3, 2, 1);
        cardNumberTextField.setOnAction(e -> pincodeField.requestFocus());


        Hyperlink supportLink = new Hyperlink("Contact with support");
        supportLink.setOnAction(e -> getHostServices().showDocument("https://jamolshyper.t.me/"));
        grid.add(supportLink, 0, 14, 2, 1);




        applyStyles();

        // Add event handler for the Login button
        loginButton.setOnAction(e -> handleLogin());
        // Add event handler for the Cancel button
        cancelButton.setOnAction(actionEvent -> closeApplication(primaryStage));
        // Add event handler for the Create account button
        createButton.setOnAction(actionEvent -> new CreateCardForm().start(new Stage()));

        loginButton.setDefaultButton(true);


        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void handleLogin() {
        String cardNumber = cardNumberTextField.getText();
        String pincode = pincodeField.getText();

        if (cardNumber.isEmpty() || pincode.isEmpty()) {
            showErrorAlert("Warning", "You need to input a card number and pin code.");
        } else {
            try {

                String sqlCommand = "SELECT * FROM `cards` WHERE `cardNumber` = '" + cardNumber + "' AND `pincode` = '" + pincode + "'";
                ResultSet resultSet = DatabaseConnection.getData(sqlCommand);
                if (!resultSet.next()) {
                    showErrorAlert("Error", "Invalid card number or pin code.");
                } else {
                    // Login successful. You can navigate to another scene here.
                    ATMMainPage atmMainPage = new ATMMainPage();
                    atmMainPage.start(new Stage());
                    System.out.println("Login successful");
                    LoginForm.cardNumber = new BigInteger(cardNumber);

                    Stage stage = (Stage) pincodeField.getScene().getWindow();
                    stage.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeApplication(Stage stage) {
        stage.close();

    }
    //blue color for login button, red color for cancel button, blue color for create button


    private void applyStyles() {
        // Applying styles for better appearance
        loginButton.setStyle("  -fx-background-color: #2196F3; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        createButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
    }
}
