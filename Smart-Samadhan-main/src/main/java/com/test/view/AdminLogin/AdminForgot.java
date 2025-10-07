package com.test.view.AdminLogin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminForgot {

    private Scene adminForgotScene;
    private Scene adminForgotOTPScene;
    private Stage adminForgotStage;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(AdminForgot.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(AdminForgot.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setAdminForgotScene(Scene scene) {
        this.adminForgotScene = scene;
    }

    public void setAdminForgotStage(Stage stage) {
        this.adminForgotStage = stage;
    }

    public VBox adminCreateScene(Runnable back) {

        Label title = new Label("Reset Password");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#007bff"));

        Text note = new Text("Enter Your Email Address to get OTP");
        note.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        note.setFill(Color.WHITE);

        Text email = new Text("email");
        email.setTranslateX(-125);
        email.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        email.setFill(Color.WHITE);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your username");
        emailField.setFont(Font.font(FONT_FAMILY, 14));
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text newPass = new Text("New Password");
        newPass.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        newPass.setFill(Color.WHITE);
        newPass.setTranslateX(-95);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Ente Your New Password");
        newPassField.setFont(Font.font(FONT_FAMILY, 14));
        newPassField.setMaxWidth(300);
        newPassField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text confirmPass = new Text("Confirm Password");
        confirmPass.setTranslateX(-75);
        confirmPass.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        confirmPass.setFill(Color.WHITE);

        TextField confirmPassWordField = new TextField();
        confirmPassWordField.setPromptText("Enter Your Conform Password");
        confirmPassWordField.setFont(Font.font(FONT_FAMILY, 14));
        confirmPassWordField.setMaxWidth(300);
        confirmPassWordField.setFocusTraversable(false);
        confirmPassWordField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Button sendOTP = new Button("Send OTP");
        sendOTP.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        sendOTP.setStyle("-fx-background-color: #007bff;-fx-text-fill:white; -fx-background-radius : 30");
        sendOTP.setMaxWidth(300);
        sendOTP.setPrefHeight(40);
        sendOTP.setPrefWidth(120);
        sendOTP.setTranslateX(-20);
        sendOTP.setFocusTraversable(false);

        sendOTP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                initializeAdminForgotOTP();
                adminForgotStage.setScene(adminForgotOTPScene);
            }
        });

        Button logInPageBtn = new Button("Log In");
        logInPageBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        logInPageBtn.setMaxWidth(300);
        logInPageBtn.setPrefHeight(40);
        logInPageBtn.setPrefWidth(120);
        logInPageBtn.setTranslateX(22);
        logInPageBtn.setFocusTraversable(false);
        logInPageBtn.setStyle("-fx-background-color: #007bff;-fx-text-fill:white; -fx-background-radius : 30");

        logInPageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                back.run();
            }
        });

        HBox h1Box = new HBox(20, sendOTP, logInPageBtn);
        h1Box.setAlignment(Pos.CENTER);
        h1Box.setPadding(new Insets(10));

        VBox v1Box = new VBox(15, email, emailField, newPass, newPassField, confirmPass, confirmPassWordField);
        v1Box.setAlignment(Pos.CENTER);
        v1Box.setPadding(new Insets(10));

        VBox vBox = new VBox(25, title, note, v1Box, h1Box);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(500);
        vBox.setMaxWidth(500);
        vBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 40;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(18, 17, 17, 0.34), 15, 0, 0, 5);"
        );

        VBox mainSignUp = new VBox(vBox);
        mainSignUp.setStyle("-fx-background-color: black;");
        mainSignUp.setAlignment(Pos.CENTER);
        return mainSignUp;
    }

    private void initializeAdminForgotOTP() {
        AdminForgotOTP otp = new AdminForgotOTP();
        otp.setForgotOTPStage(adminForgotStage);
        adminForgotOTPScene = new Scene(otp.createForgotOTP(this::handleBack), 1550, 800);
        otp.setForgotOTPScene(adminForgotOTPScene);
    }

    private void handleBack() {
        adminForgotStage.setScene(adminForgotScene);
    }
}