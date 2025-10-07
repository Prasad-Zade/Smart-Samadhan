package com.test.view.AdminLogin;

import com.test.Controller.AdminLoginControl;
import com.test.view.LoginPage.ForgotPass;
import com.test.view.adminPage.AdminHome;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminLogin {

    private Scene adminScene;
    private Stage adminStage;

    private final AdminLoginControl adminloginControl = new AdminLoginControl();
    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(AdminLogin.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(AdminLogin.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setAdminScene(Scene scene) {
        this.adminScene = scene;
    }

    public void setAdminStage(Stage stage) {
        this.adminStage = stage;
    }

    public HBox createScene(Runnable backUser) {

        ImageView imageView = new ImageView("assets/Images/social-media-network.png");
        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(60);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPadding(new Insets(20));

        ImageView logo = new ImageView("assets\\Images\\Untitled_design__7_-removebg-preview.png");
        logo.setFitWidth(300);
        logo.setPreserveRatio(true);
        logo.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Label slogan = new Label("ADMIN");
        slogan.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        slogan.setStyle("-fx-text-fill: #007bff;");
        slogan.setTranslateY(-20);

        Label userLabel = new Label("Email:");
        userLabel.setTranslateX(-125);
        userLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        userLabel.setTextFill(Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setFont(Font.font(FONT_FAMILY, 14));
        usernameField.setMaxWidth(300);
        usernameField.setFocusTraversable(false);
        usernameField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Label passLabel = new Label("Password:");
        passLabel.setTranslateX(-110);
        passLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        passLabel.setTextFill(Color.WHITE);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(Font.font(FONT_FAMILY, 14));
        passwordField.setMaxWidth(300);
        passwordField.setFocusTraversable(false);
        passwordField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text forgotPass = new Text("Forgot Password ?");
        forgotPass.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));
        forgotPass.setTranslateX(90);
        forgotPass.setFill(Color.RED);
        forgotPass.setUnderline(true);

        forgotPass.setOnMouseClicked(event -> initializeForgotPass());

        Button back = new Button("Back");
        back.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        back.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);-fx-text-fill:white; -fx-background-radius : 30");
        back.setPrefHeight(40);
        back.setPrefWidth(120);
        back.setTranslateX(-22);
        back.setOnAction(e -> backUser.run());

        Button logInButton = new Button("Log In");
        logInButton.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        logInButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);-fx-text-fill:white; -fx-background-radius : 30");
        logInButton.setPrefHeight(40);
        logInButton.setPrefWidth(120);
        logInButton.setTranslateX(22);

        Label resultLabel = new Label();
        resultLabel.setTextFill(Color.LIMEGREEN);
        resultLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));

        logInButton.setOnAction(e -> {
            String email = usernameField.getText();
            String password = passwordField.getText();
            boolean success = adminloginControl.LogInWithGmailAndPassword(email, password);

            if (success) {
                resultLabel.setTextFill(Color.LIMEGREEN);
                resultLabel.setText("Login Successful");
                initializeAdminHomePage();
            } else {
                resultLabel.setTextFill(Color.ORANGERED);
                resultLabel.setText("Login Failed");
            }
        });

        HBox v1Box = new HBox(20, back, logInButton);
        v1Box.setAlignment(Pos.CENTER);
        v1Box.setPadding(new Insets(10));

        VBox v2Box = new VBox(15, logo, slogan, userLabel, usernameField, passLabel, passwordField);
        v2Box.setAlignment(Pos.CENTER);

        VBox mainVBox = new VBox(20, v2Box, forgotPass, v1Box, resultLabel);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setPrefWidth(500);
        mainVBox.setMaxHeight(100);
        mainVBox.setPadding(new Insets(0, 0, 30, 0));
        mainVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);-fx-background-radius: 20;");

        HBox mainHBox = new HBox(10, mainVBox, imageBox);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setStyle("-fx-background-color: black;");
        return mainHBox;
    }

    private void initializeAdminHomePage() {
        AdminHome adminHome = new AdminHome();
        Scene homeScene = adminHome.getHomeScene(adminStage);
        adminStage.setScene(homeScene);
    }

    private void initializeForgotPass() {
        ForgotPass forgotPassView = new ForgotPass();
    
        VBox forgotPassLayout = forgotPassView.createScene(this::handleForgotButton);
        Scene newForgotPassScene = new Scene(forgotPassLayout, 1550, 800);
        adminStage.setScene(newForgotPassScene);
    }

    
    private void handleForgotButton() {
        
        if (adminScene != null) {
            adminStage.setScene(adminScene);
        } else {
            System.err.println("Error: Admin scene is not initialized. Cannot go back.");
        }
    }
}