package com.test.view.LoginPage;

import com.test.Controller.UserAuthController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ForgotPass {

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(ForgotPass.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ForgotPass.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createScene(Runnable back) {

        Label title = new Label("Reset Password");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#007bff"));

        Text note = new Text("Enter your email address to receive a reset link.");
        note.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        note.setFill(Color.WHITE);

        Text email = new Text("Email:");
        email.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        email.setTranslateX(-125);
        email.setFill(Color.WHITE);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your registered email");
        emailField.setFont(Font.font(FONT_FAMILY, 14));
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);

        Button sendLinkButton = new Button("Send Reset Link");
        sendLinkButton.setStyle(
                "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);");
        sendLinkButton.setPrefHeight(40);
        sendLinkButton.setPrefWidth(200);

        Button logInPageBtn = new Button("Back to Log In");
        logInPageBtn.setPrefHeight(40);
        logInPageBtn.setPrefWidth(200);
        logInPageBtn.setStyle(
                "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-background-color: #555555;");

        sendLinkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    note.setText("Please enter your email address.");
                    note.setFill(Color.ORANGERED);
                    return;
                }

                loadingIndicator.setVisible(true);
                sendLinkButton.setDisable(true);
                logInPageBtn.setDisable(true);

                Task<String> sendLinkTask = new Task<>() {
                    @Override
                    protected String call() throws Exception {
                        UserAuthController authController = new UserAuthController();
                        return authController.sendPasswordResetEmail(email);
                    }
                };

                sendLinkTask.setOnSucceeded(e -> {
                    Platform.runLater(() -> {
                        loadingIndicator.setVisible(false);
                        String result = sendLinkTask.getValue();

                        // CORRECTED: The error code from the Admin SDK is "USER_NOT_FOUND"
                        if ("SUCCESS".equals(result)) {
                            title.setText("Link Sent!");
                            note.setText(
                                    "A reset link has been sent. Check your email inbox (and spam folder) to continue.");
                            note.setFill(Color.LIGHTGREEN);
                            emailField.setDisable(true);
                        } else if ("USER_NOT_FOUND".equals(result)) {
                            note.setText("No account was found with this email.");
                            note.setFill(Color.ORANGERED);
                            sendLinkButton.setDisable(false);
                            logInPageBtn.setDisable(false);
                        } else {
                            note.setText("An unexpected error occurred. Please try again.");
                            note.setFill(Color.ORANGERED);
                            sendLinkButton.setDisable(false);
                            logInPageBtn.setDisable(false);
                        }
                    });
                });

                sendLinkTask.setOnFailed(e -> {
                Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                note.setText("A critical error occurred. Check the logs.");
                note.setFill(Color.RED);
                sendLinkButton.setDisable(false);
                logInPageBtn.setDisable(false);
                });
                });
               
                new Thread(sendLinkTask).start();

            }
        });

        logInPageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                back.run();
            }
        });

        HBox buttonBox = new HBox(20, sendLinkButton, logInPageBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(15, email, emailField);
        contentBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(25, title, note, contentBox, loadingIndicator, buttonBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxHeight(500);
        vBox.setMaxWidth(500);
        vBox.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 40;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(18, 17, 17, 0.34), 15, 0, 0, 5);");

        VBox mainLayout = new VBox(vBox);
        mainLayout.setStyle("-fx-background-color: black;");
        mainLayout.setAlignment(Pos.CENTER);

        return mainLayout;
    }
}