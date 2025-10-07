package com.test.view.adminPage;

import com.test.view.AdminLogin.AdminLogin;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminHome {
    private Stage primaryStage;
    private BorderPane borderPane;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(AdminHome.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(AdminHome.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Scene getHomeScene(Stage stage) {
        this.primaryStage = stage;
        Parent root = adminUI();
        return new Scene(root, 1550, 800);
    }

    private Parent adminUI() {
        borderPane = new BorderPane();
        borderPane.setLeft(buildSidebar());
        borderPane.setCenter(new Dashboard().createScene());
        borderPane.setPadding(new Insets(80));
        borderPane.setStyle("-fx-background-color: black;");
        return borderPane;
    }

    private VBox buildSidebar() {
        Image logo = new Image("assets\\Images\\Untitled_design__7_-removebg-preview.png");
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(50);

        VBox imgContainer = new VBox(imageView);
        imgContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");
        imgContainer.setMaxWidth(300);
        imgContainer.setPrefHeight(100);

        Button dashboardButton = createNavButton("Dashboard");
        dashboardButton.setOnAction(e -> borderPane.setCenter(new Dashboard().createScene()));

        Button manageUsersButton = createNavButton("Manage Users");
        manageUsersButton.setOnAction(e -> borderPane.setCenter(new ManageUsersPage().createUserManagementScene()));


        Button pendingButton = createNavButton("Pending");
        pendingButton.setOnAction(e -> borderPane.setCenter(new PendingPage().createScenePending()));

        Button completedButton = createNavButton("Completed");
        completedButton.setOnAction(e -> borderPane.setCenter(new Completed().createSceneCompleted()));

        Button rejectedButton = createNavButton("Rejected");
        rejectedButton.setOnAction(e -> borderPane.setCenter(new RejectedPage().createSceneRejected()));

        Button logoutButton = createNavButton("Logout");
        logoutButton.setOnAction(event -> {
            AdminLogin adminLogin = new AdminLogin();
            adminLogin.setAdminStage(primaryStage);
            Scene loginScene = new Scene(adminLogin.createScene(() -> {}), 1550, 800);
            primaryStage.setScene(loginScene);
        });

        VBox buttonContainer = new VBox(30, dashboardButton, manageUsersButton, pendingButton, completedButton, rejectedButton, logoutButton);
        buttonContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");
        buttonContainer.setPrefHeight(550);
        buttonContainer.setMinWidth(300);
        buttonContainer.setPadding(new Insets(10, 0, 0, 40));

        VBox leftSidebar = new VBox(30, imgContainer, buttonContainer);
        leftSidebar.setPadding(new Insets(20));
        leftSidebar.setStyle("-fx-background-color: transparent;");

        return leftSidebar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        String defaultStyle = "-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: transparent;";
        String hoverStyle = "-fx-text-fill: #007bff; -fx-font-weight: bold; -fx-font-size: 24; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: transparent;";

        button.setStyle(defaultStyle);
        button.setPadding(new Insets(3, 0, 0, 30));
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        button.setTranslateX(20);
        button.setTranslateY(15);
        return button;
    }
}