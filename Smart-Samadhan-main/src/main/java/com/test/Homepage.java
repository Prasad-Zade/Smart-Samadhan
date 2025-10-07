package com.test;

import com.test.view.LoginPage.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Homepage extends Application {

    @Override
    public void start(Stage primaryStage) {
        Login loginpage = new Login();
        Scene LoginScene = loginpage.createScene(primaryStage);

        primaryStage.setTitle("Smart Samadhan");
        primaryStage.setScene(LoginScene);
        primaryStage.getIcons().add(new javafx.scene.image.Image("assets/Images/Untitled_design__7_-removebg-preview.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
    
}
