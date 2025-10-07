package com.test.view.Home;

import com.test.util.LanguageManager;
import com.test.view.LoginPage.Login;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Settings {

    private Stage primaryStage;
    private Scene settingScene;
    private BorderPane bp;
    private String currentIdToken;
    private String currentEmail;
    
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Settings.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Settings.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setSettingStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettingScene(Scene settingScene) {
        this.settingScene = settingScene;
    }

    public void setAuthInfo(String idToken, String email) {
        this.currentIdToken = idToken;
        this.currentEmail = email;
    }

    public Parent createSettingsScene(Runnable back, BorderPane bp, Stage stage) {
        this.primaryStage = stage;
        this.bp = bp;

        Button changepassword = new Button(lang.getString("settings.changePassword"));
        changepassword.setFocusTraversable(false);
        changepassword.setMaxWidth(200);
        changepassword.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        changepassword.setOnAction(e -> {

            Changepassword cp = new Changepassword();
            cp.setAuthInfo(currentIdToken, currentEmail);
            bp.setCenter(cp.createResetPasswordUI(this::handleBackButton));
        });

        Button giveFeedbackBtn = new Button(lang.getString("settings.giveFeedback"));
        giveFeedbackBtn.setMaxWidth(200);
        giveFeedbackBtn.setFocusTraversable(false);
        giveFeedbackBtn.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        giveFeedbackBtn.setOnAction(e -> {
            Feedback feedback = new Feedback();
            bp.setCenter(feedback.createFeedbackScene(this::handleBackButton));
        });

        Button showFeedbackBtn = new Button(lang.getString("settings.showFeedback"));
        showFeedbackBtn.setMaxWidth(200);
        showFeedbackBtn.setFocusTraversable(false);
        showFeedbackBtn.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        showFeedbackBtn.setOnAction(e -> {
    
            FeedbackList feedbackList = new FeedbackList();
            bp.setCenter(feedbackList.createFeedbackListScene(this::handleBackButton));
        });

        Button logout = new Button(lang.getString("settings.logoutButton"));
        logout.setMaxWidth(200);
        logout.setFocusTraversable(false);
        logout.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #dc3545, #bd2130);"
        );
        logout.setOnAction(e -> {
            Login loginPage = new Login();
            loginPage.setStage(primaryStage);
            Scene loginScene = loginPage.createScene(primaryStage);
            primaryStage.setScene(loginScene);
        });

        VBox settingVBox = new VBox(changepassword, giveFeedbackBtn, showFeedbackBtn, logout);
        settingVBox.setAlignment(Pos.CENTER);
        settingVBox.setSpacing(20);
        settingVBox.setPadding(new Insets(30));
        settingVBox.setPrefHeight(600);
        settingVBox.setPrefWidth(800);
        settingVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        VBox container = new VBox(settingVBox);
        container.setPadding(new Insets(80));
        container.setStyle("-fx-background-color: transparent;");

        return container;
    }

    private void handleBackButton() {
    
        bp.setCenter(createSettingsScene(null, bp, primaryStage));
    }
}