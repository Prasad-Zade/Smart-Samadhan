package com.test.view.Home;

import com.test.Controller.LoginControl;
import com.test.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Changepassword {

    private Stage primaryStage;
    private Scene aboutScene, homeScene;
    private String currentIdToken;
    private String currentEmail;

    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Changepassword.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Changepassword.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setPasswordScene(Scene aboutScene) {
        this.aboutScene = aboutScene;
    }

    public void setAuthInfo(String idToken, String email) {
        this.currentIdToken = idToken;
        this.currentEmail = email;
    }

    public Parent createResetPasswordUI(Runnable onBack) {

        Label title = new Label(lang.getString("changePassword.title"));
        title.setStyle("-fx-font-size:30;-fx-font-weight:bold;-fx-text-fill: #007bff;-fx-font-family: '" + FONT_FAMILY + "';");

        Text note = new Text(lang.getString("changePassword.note"));
        note.setStyle("-fx-font-size:30;-fx-font-weight:bold;-fx-fill:white;-fx-font-family: '" + FONT_FAMILY + "';");

        Text gmailLabel = new Text(lang.getString("changePassword.emailLabel"));
        gmailLabel.setTranslateX(-125);
        gmailLabel.setStyle("-fx-font-size:15;-fx-font-weight:bold;-fx-fill:white;-fx-font-family:'" + FONT_FAMILY + "';");

        TextField gmailField = new TextField();
        gmailField.setPromptText(lang.getString("changePassword.emailPrompt"));
        gmailField.setFont(Font.font(FONT_FAMILY, 14));
        gmailField.setMaxWidth(300);
        gmailField.setEditable(false);
        gmailField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text newPassLabel = new Text(lang.getString("changePassword.newPasswordLabel"));
        newPassLabel.setStyle("-fx-font-size:15;-fx-font-weight:bold;-fx-fill:white;-fx-font-family:'" + FONT_FAMILY + "';");
        newPassLabel.setTranslateX(-95);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText(lang.getString("changePassword.newPasswordPrompt"));
        newPassField.setFont(Font.font(FONT_FAMILY, 14));
        newPassField.setMaxWidth(300);
        newPassField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text confirmPassLabel = new Text(lang.getString("changePassword.confirmPasswordLabel"));
        confirmPassLabel.setStyle("-fx-font-size:15;-fx-font-weight:bold;-fx-fill:white;-fx-font-family:'" + FONT_FAMILY + "';");
        confirmPassLabel.setTranslateX(-85);

        PasswordField confirmPassWordField = new PasswordField();
        confirmPassWordField.setPromptText(lang.getString("changePassword.confirmPasswordPrompt"));
        confirmPassWordField.setFont(Font.font(FONT_FAMILY, 14));
        confirmPassWordField.setMaxWidth(300);
        confirmPassWordField.setFocusTraversable(false);
        confirmPassWordField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Label messageLabel = new Label("");
        messageLabel.setTextFill(Color.RED);
        messageLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));

        Button changePasswordButton = new Button(lang.getString("changePassword.changeButton"));
        changePasswordButton.setFocusTraversable(false);
        changePasswordButton.setStyle("-fx-font-size: 15px; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-radius: 10;-fx-background-color:#007bff;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-family: '" + FONT_FAMILY + "';");

        Button backButton = new Button(lang.getString("changePassword.backButton"));
        backButton.setOnAction(e -> {
            onBack.run();
            newPassField.clear();
            confirmPassWordField.clear();
            messageLabel.setText("");
        });
        backButton.setFocusTraversable(false);
        backButton.setStyle("-fx-font-size: 15px; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-radius: 10;-fx-background-color:#007bff;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-family: '" + FONT_FAMILY + "';");

        changePasswordButton.setOnAction(e -> {
            String newPassword = newPassField.getText();
            String confirmPassword = confirmPassWordField.getText();

            if (currentIdToken == null || currentIdToken.isEmpty()) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(lang.getString("changePassword.error.notAuthenticated"));
                return;
            }
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(lang.getString("changePassword.error.fieldsEmpty"));
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(lang.getString("changePassword.error.passwordsMismatch"));
                return;
            }
            if (newPassword.length() < 6) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(lang.getString("changePassword.error.passwordTooShort"));
                return;
            }

            LoginControl loginControl = new LoginControl();
            boolean success = loginControl.changePassword(currentIdToken, newPassword);

            if (success) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText(lang.getString("changePassword.success.passwordChanged"));
                newPassField.clear();
                confirmPassWordField.clear();
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(lang.getString("changePassword.error.failedToChange"));
            }
        });

        if (currentEmail != null) {
            gmailField.setText(currentEmail);
        }

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");
        layout.setMaxHeight(480);
        layout.setMaxWidth(910);

        layout.getChildren().addAll(title, note,gmailLabel, gmailField,newPassLabel, newPassField,confirmPassLabel, confirmPassWordField,changePasswordButton,messageLabel,backButton);

        return layout;
    }
}