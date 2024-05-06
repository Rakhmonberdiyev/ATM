package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Random;

public class CreateCardForm extends Application {

    private TextField name_Text_Field = new TextField();
    public TextField card_Number_Text_Field = new TextField();
    private TextField phone_Text_Field = new TextField();
    private TextField pincode_Tex_Field = new TextField();
    private TextField balance_Tex_Field = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        name_Text_Field.setPrefWidth(150);
        phone_Text_Field.setPrefWidth(150);
        card_Number_Text_Field.setPrefWidth(150);
        pincode_Tex_Field.setPrefWidth(150);
        balance_Tex_Field.setPrefWidth(150);
        primaryStage.setTitle("Create Card Form");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);"); // Light blue background color

        // Labels
        Label name_Label = new Label("Name:");
        Label address_Label  = new Label("Pincode:");
        Label email_Label = new Label("Card number:");
        Label phone_Label = new Label("Phone:");
        Label balance_Label = new Label("Balance:");
        // Add labels and text field to the grid
        grid.add(name_Label, 0, 0);
        grid.add(name_Text_Field, 1, 0);
        // Add labels and text fields to the grid
        grid.add(address_Label, 0, 1);
        grid.add(pincode_Tex_Field,1,1);
        // Add labels and text fields to the grid
        grid.add(email_Label, 0, 2);
        grid.add(card_Number_Text_Field, 1, 2);
        card_Number_Text_Field.setText(String.valueOf(generateCardNumber()));
        // Add labels and text fields to the grid
        grid.add(phone_Label, 0, 3);
        grid.add(phone_Text_Field, 1, 3);
        // Add labels and text fields to the grid
        grid.add(balance_Label, 0, 4);
        grid.add(balance_Tex_Field, 1, 4);

        // Buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        //use the Enter key to submit the form
        saveButton.setDefaultButton(true);


        saveButton.setOnAction(event -> {
            try {
                saveCardDetails();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        cancelButton.setOnAction(event -> {
            primaryStage.close(); // Close the window
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        grid.add(buttonBox, 0, 5, 2, 1); // span 2 columns

        // Applying styles for better appearance
        saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

//    generate random card number starts with 9860 0r 8600 and ends with 12 digits

    private long generateCardNumber() {
            Random random = new Random(); // create a Random object
            int prefix = random.nextInt(2); // generate a random integer between 0 and 1
            long suffix = random.nextLong(10000000000000L); // generate a random long between 0 and 9999999999999
            String formattedSuffix = String.format("%012d", suffix); // format the suffix with 12 digits and leading zeros
            String result;
            if (prefix == 0) {
                result = "9860" + formattedSuffix; // if the prefix is 0, the first four digits are 9860
            } else {
                result = "8600" + formattedSuffix; } // if the prefix is 1, the first four digits are 8600

        return Long.parseLong(result);}  // return result


    private void saveCardDetails() throws SQLException {
        String name = name_Text_Field.getText();
        String cardNumber = card_Number_Text_Field.getText();
        String phone = phone_Text_Field.getText();
        String pincode = pincode_Tex_Field.getText();
        String balance = balance_Tex_Field.getText();
        String sqlCommand1 = "SELECT * FROM `cards` WHERE `cardNumber` = '"+cardNumber+"'";

        if (DatabaseConnection.getData(sqlCommand1).next()){
            System.out.println("Card number already exists");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Card number already exists");
            alert.showAndWait();
            return;
        }
        String sqlCommand = "INSERT INTO `cards` (`name`, `cardNumber`, `phone`, `pincode`,`balance`) VALUES ('" + name + "', '" + cardNumber + "', '" + phone + "', '" + pincode + "','"+balance+"')";
        DatabaseConnection.saveData(sqlCommand);

        Stage stage = (Stage) name_Text_Field.getScene().getWindow();
        stage.close();
        LoginForm loginForm = new LoginForm();
        loginForm.start(new Stage());
        System.out.println("Saving card credentials: " + name + " - " + cardNumber + " - " + phone + " - " + pincode+"-"+balance);
    }
}

