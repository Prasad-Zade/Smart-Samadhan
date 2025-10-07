package com.test.view.LoginPage;

import com.test.Controller.LoginControl;
import com.test.dao.ProfileDetailData;
import com.test.view.AdminLogin.AdminLogin;
import com.test.view.Home.HomeMain;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Login {
    
    private Scene loginScene, signUpScene, adminLogInScene, homeScene;
    private Stage primaryStage;
    public static String userEmail;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Login.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Login.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
            Font.loadFont(Login.class.getResourceAsStream("/assets/fonts/Poppins-Italic.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins fonts. Please ensure Poppins-Regular.ttf, Poppins-Bold.ttf, and Poppins-Italic.ttf exist in the assets/fonts directory.");
        }
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public void setScene(Scene scene) {
        loginScene = scene;
    }

    HomeMain hm = new HomeMain();

    public Scene createScene(Stage stage) {
        this.primaryStage = stage;
        hm.setStage(primaryStage);
        HBox root = createHBox();
        loginScene = new Scene(root, 1550, 800);
        hm.setScene(loginScene);
        return loginScene;
    }

    LoginControl logInControl = new LoginControl();

    public HBox createHBox() {
        ImageView imageView = new ImageView("assets/Images/social-media-network.png");
        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(60);

        Label sloganLabel = new Label("Report. Resolve. Reform.");
        sloganLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 40));
        sloganLabel.setStyle("-fx-text-fill: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);");
        sloganLabel.setTranslateX(60);
        sloganLabel.setTranslateY(30);
        sloganLabel.setOpacity(0);

        FadeTransition ft = new FadeTransition(Duration.millis(2000), sloganLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

        VBox imageBox = new VBox(imageView, sloganLabel);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPadding(new Insets(20));

        ImageView logo = new ImageView("assets/Images/Untitled_design__7_-removebg-preview.png");
        logo.setFitWidth(300);
        logo.setPreserveRatio(true);
        logo.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Label slogan = new Label("WELCOME");
        slogan.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        slogan.setStyle("-fx-text-fill: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);");
        slogan.setTranslateY(-40);

        Label userLabel = new Label("Email:");
        userLabel.setTranslateX(-125);
        userLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        userLabel.setTextFill(Color.WHITE);

        Text user = new Text();
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setFont(Font.font(FONT_FAMILY, 14));
        usernameField.setMaxWidth(300);
        usernameField.setFocusTraversable(false);
        usernameField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.17), 15, 0, 0, 5);");

        Label passLabel = new Label("Password:");
        passLabel.setTranslateX(-110);
        passLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        passLabel.setTextFill(Color.WHITE);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(Font.font(FONT_FAMILY, 14));
        passwordField.setMaxWidth(300);
        passwordField.setFocusTraversable(false);
        passwordField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.17), 15, 0, 0, 5);");

        Text forgotPass = new Text("Forgot Password ?");
        forgotPass.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));
        forgotPass.setTranslateX(90);
        forgotPass.setTranslateY(-30);
        forgotPass.setFill(Color.RED);
        forgotPass.setUnderline(true);
        forgotPass.setOnMouseClicked(event -> initializeForgotPass());

        Button logInButton = new Button("Log in");
        logInButton.setLayoutX(665);
        logInButton.setLayoutY(500);
        logInButton.setStyle(
                "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        logInButton.setPrefHeight(40);
        logInButton.setPrefWidth(120);
        logInButton.setTranslateX(-22);

        Label resultLabel = new Label();
        resultLabel.setTranslateY(-350);
        resultLabel.setTextFill(Color.GREEN);
        resultLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 12));

        logInButton.setOnAction(e -> {
            userEmail = usernameField.getText().trim();
            String password = passwordField.getText();

            if (userEmail.isEmpty() || password.isEmpty()) {
                resultLabel.setText("Please enter both email and password");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            try {
                String idToken = logInControl.LogInWithGmailAndPassword(userEmail, password);
                if (idToken != null) {
                    ProfileDetailData.loadUserDetails(userEmail);
                    resultLabel.setText("Login Successful!");
                    resultLabel.setTextFill(Color.GREEN);
                    initializeHomePage(idToken, userEmail);
                    primaryStage.setScene(homeScene);
                } else {
                    resultLabel.setText("Login Failed: Invalid email or password.");
                    resultLabel.setTextFill(Color.RED);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("An error occurred. Please try again.");
                resultLabel.setTextFill(Color.RED);
            }
        });

        Button signUpButton = new Button("Sign up");
        signUpButton.setLayoutX(665);
        signUpButton.setLayoutY(500);
        signUpButton.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        signUpButton.setPrefHeight(40);
        signUpButton.setPrefWidth(120);
        signUpButton.setTranslateX(22);

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                initializePage2();
                primaryStage.setScene(signUpScene);
            }
        });

        Text adminLogin = new Text("Admin LogIn");
        adminLogin.setTranslateX(0);
        adminLogin.setTranslateY(-30);
        adminLogin.setStyle(
                "-fx-fill: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        adminLogin.setUnderline(true);

        adminLogin.setOnMouseClicked(event -> {
            initializeAdminLogIn();
            primaryStage.setScene(adminLogInScene);
        });

        VBox v3Box = new VBox(10, passwordField, forgotPass);
        v3Box.setAlignment(Pos.CENTER);
        v3Box.setPadding(new Insets(10));

        HBox v1Box = new HBox(20, logInButton, signUpButton);
        v1Box.setAlignment(Pos.CENTER);
        v1Box.setPadding(new Insets(10));

        VBox v2Box = new VBox(15, logo, slogan, userLabel, usernameField, passLabel, passwordField, user);
        v2Box.setAlignment(Pos.CENTER);

        VBox mainVBox = new VBox(15, v2Box, forgotPass, v1Box, resultLabel, adminLogin);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setPrefWidth(500);
        mainVBox.setMaxHeight(100);
        mainVBox.setStyle(
                "-fx-background-color:linear-gradient(to bottom, #1b2735, #090a0f);" +
                        "-fx-background-radius: 20;"
        );

        HBox mainHBox = new HBox(10, mainVBox, imageBox);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setStyle("-fx-background-color: black;");

        return mainHBox;
    }

    private void initializePage2() {
        if (loginScene == null) {
            loginScene = new Scene(createHBox(), 1550, 800);
        }
        SignUp page2 = new SignUp();
        page2.setStage(primaryStage);
        signUpScene = new Scene(page2.createScene(this::handleBackButton), 1550, 800);
        page2.setScene(signUpScene);
    }

    private void handleBackButton() {
        primaryStage.setScene(loginScene);
    }

    private void initializeForgotPass() {
        ForgotPass forgotPassView = new ForgotPass();
        VBox forgotPassLayout = forgotPassView.createScene(this::handleForgotButton);
        Scene newForgotPassScene = new Scene(forgotPassLayout, 1550, 800);
        primaryStage.setScene(newForgotPassScene);
    }

    private void handleForgotButton() {
        primaryStage.setScene(loginScene);
    }

    private void initializeAdminLogIn() {
        AdminLogin admin = new AdminLogin();
        admin.setAdminStage(primaryStage);
        adminLogInScene = new Scene(admin.createScene(this::handleAdminButton), 1550, 800);
        admin.setAdminScene(adminLogInScene);
    }

    private void handleAdminButton() {
        primaryStage.setScene(loginScene);
    }

    private void initializeHomePage(String idToken, String email) {
        hm.setAuthInfo(idToken, email);
        homeScene = hm.getHomeScene(primaryStage);
    }
}