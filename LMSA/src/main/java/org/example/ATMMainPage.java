package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ATMMainPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primary_stage) {
        primary_stage.setTitle("ATM");

        BorderPane border_pane = new BorderPane();

        // Create top menu bar
        MenuBar menu_bar = new MenuBar();
        Menu file_menu = new Menu("File");
        MenuItem exit_menu_item = new MenuItem("Exit");
        Menu login_menu = new Menu("Login");
        MenuItem login_menu_item = new MenuItem("Login");

        login_menu_item.setOnAction(e -> {
            LoginForm login_form = new LoginForm();
            login_form.start(new Stage());
            primary_stage.close();
        });
        exit_menu_item.setOnAction(e -> System.exit(0));
        file_menu.getItems().add(exit_menu_item);
        menu_bar.getMenus().add(file_menu);
        login_menu.getItems().add(login_menu_item);
        menu_bar.getMenus().add(login_menu);

        // Create main content area with a lighter blue background
        VBox content_vbox = new VBox();
        content_vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #ADD8E6);"); // Set a lighter blue background
        content_vbox.setAlignment(Pos.CENTER);
        content_vbox.setSpacing(20);

        Label welcome_label = new Label("Welcome to ATM!");
        welcome_label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ecf0f1;"); // Set font color to white

        Button check_balance_button = create_styled_button("Check Balance");
        Button take_cash_button = create_styled_button("Take Cash");
        Button change_pin_button = create_styled_button("Change PIN");
        Button transfer_button = create_styled_button("Transfer");
        Button deposit_button = create_styled_button("Deposit");

        check_balance_button.setOnAction(e -> check_balance_form());
        take_cash_button.setOnAction(e -> take_cash_from());
        change_pin_button.setOnAction(e -> change_pin_form());
        transfer_button.setOnAction(e -> transfer_form());
        deposit_button.setOnAction(e -> deposit_form());

        ImageView uni_image_view = create_image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNo3diS-3T1qt9dEZwx6ZZmopn5ctR-wL-TWvLAZGPkQ&s");


        content_vbox.getChildren().addAll(welcome_label, check_balance_button, take_cash_button, change_pin_button, transfer_button, deposit_button,
                uni_image_view);

        Scene scene = new Scene(border_pane, 800, 600);
        scene.setFill(Color.web("#8ab4e3")); // Lightened deep blue background

        // Set up the BorderPane layout
        border_pane.setTop(menu_bar);
        border_pane.setCenter(content_vbox);

        primary_stage.setScene(scene);
        primary_stage.show();
    }

    private Button create_styled_button(String text) {
        Button button = new Button(text);
        button.setPrefSize(150, 50);
        button.setStyle("-fx-background-color: #3b5998; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;"); // Set button color to a slightly darker blue
        return button;
    }

    private ImageView create_image(String image_url) {
        Image image = new Image(image_url);
        ImageView image_view = new ImageView(image);
        image_view.setFitWidth(150);
        image_view.setFitHeight(150);
        return image_view;
    }

    private void check_balance_form() {
        CheckBalanceForm check_balance_form = new CheckBalanceForm();
        check_balance_form.start(new Stage());

        System.out.println("Showing check balance form");
    }

    private void take_cash_from() {
        WithdrawForm withdraw_from = new WithdrawForm();
        withdraw_from.start(new Stage());

        System.out.println("showing withdraw form");
    }

    private void change_pin_form() {
        ChangePin change_pin = new ChangePin();
        change_pin.start(new Stage());
        System.out.println("Showing change pin form");
    }

    private void transfer_form() {
        TransferForm transfer_form = new TransferForm();
        transfer_form.start(new Stage());
        System.out.println("Showing transfer form");
    }

    private void deposit_form() {
        DepositForm deposit_form = new DepositForm();
        deposit_form.start(new Stage());
        System.out.println("Showing deposit form");
    }
}