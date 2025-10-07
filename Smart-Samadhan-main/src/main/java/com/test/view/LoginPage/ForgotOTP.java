package com.test.view.LoginPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ForgotOTP {

    private Stage forgotOTPStage;
    private Scene forgotOTPScene;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(ForgotOTP.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ForgotOTP.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setForgotOTPStage(Stage forgotOTPStage) {
        this.forgotOTPStage = forgotOTPStage;
    }

    public void setForgotOTPScene(Scene forgotOTPScene) {
        this.forgotOTPScene = forgotOTPScene;
    }

    public VBox createForgotOTP(Runnable backAction) {

        Text title = new Text("Enter Your 4-Digit OTP");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        title.setFill(Color.web("#007bff"));

        TextField otpField = new TextField();
        otpField.setPromptText("Enter your OTP");
        otpField.setFont(Font.font(FONT_FAMILY, 14));
        otpField.setMaxWidth(300);
        otpField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Button backLogInPage = new Button("Back");
        backLogInPage.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        backLogInPage.setStyle("-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff);-fx-text-fill:white; -fx-background-radius : 30");
        backLogInPage.setPrefHeight(40);
        backLogInPage.setPrefWidth(200);

        backLogInPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                backAction.run();
            }
        });

        VBox vBox = new VBox(20, title, otpField, backLogInPage);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(300);
        vBox.setMaxWidth(500);
        vBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 40;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(18, 17, 17, 0.34), 15, 0, 0, 5);"
        );

        VBox fotp = new VBox(vBox);
        fotp.setStyle("-fx-background-color: black;");
        fotp.setAlignment(Pos.CENTER);
        return fotp;
    }
}