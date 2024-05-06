package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ChangePin extends Application {

    private Label message_label;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primary_stage) {

        Label new_pin_label = new Label("New PIN:");
        PasswordField new_pin_field = new PasswordField();

        Label confirm_pin_label = new Label("Confirm New PIN:");
        PasswordField confirm_pin_field = new PasswordField();

        Button change_pin_button = new Button("Change PIN");
        change_pin_button.setDefaultButton(true);

        message_label = new Label("");

        // Create layout grid with light blue background
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);"); // Light blue background color

        grid.add(new_pin_label, 0, 1);
        grid.add(new_pin_field, 1, 1);
        grid.add(confirm_pin_label, 0, 2);
        grid.add(confirm_pin_field, 1, 2);
        grid.add(change_pin_button, 1, 3);
        grid.add(message_label, 0, 4, 2, 1);

        // Applying styles for better appearance
        change_pin_button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        // Set button action
        change_pin_button.setOnAction(event -> change_pin(new_pin_field.getText(), confirm_pin_field.getText()));

        new_pin_field.setOnAction(e -> confirm_pin_field.requestFocus());
        confirm_pin_field.setOnAction(e -> change_pin_button.fire());

        Button cancel_button = new Button("Cancel");
        cancel_button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white");
        grid.add(cancel_button, 2, 3);
        cancel_button.setOnAction(e -> {
            primary_stage.close();

            // Show alert box to confirm success
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Success");
        });

        Scene scene = new Scene(grid, 400, 250);
        primary_stage.setScene(scene);

        primary_stage.setTitle("Change PIN");
        primary_stage.show();
    }

    private void change_pin(String new_pin, String confirm_pin) {
        if (new_pin.equals(confirm_pin)) {
            String sql_command = "UPDATE cards SET pincode = '" + new_pin + "' WHERE cardNumber = '" + LoginForm.getCardNumber() + "'";
            try {
                DatabaseConnection.updateData(sql_command);
                set_message("PIN changed successfully!");
                Stage stage = (Stage) message_label.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            set_message("PINs do not match!");
        }
    }

    private void set_message(String message) {
        message_label.setText(message);
    }
}
