package com.test.view.Post;

import com.test.view.Home.HomeMain;
import com.test.util.LanguageManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PostButton {

    private Stage primaryStage;
    private Scene uploadScene;

    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(PostButton.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(PostButton.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Scene getUploadScene(Stage stage) {
        this.primaryStage = stage;

        Pane uploadPane = new Pane();

        
        Button backToHome = new Button(lang.getString("postButton.backButton"));
        backToHome.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );
        backToHome.setMaxHeight(20);
        backToHome.setMinWidth(50);

        backToHome.setOnAction(e -> {
            HomeMain homePage = new HomeMain();
            homePage.setStage(primaryStage);
            Scene homeScene = homePage.getHomeScene(primaryStage);
            primaryStage.setScene(homeScene);
        });

        backToHome.setTranslateX(30);
        backToHome.setTranslateY(30);

        Image img = new Image("assets/Images/image_(3)[1][1].png");
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(480);
        imageView.setLayoutY(100);

       
        Text tx = new Text(lang.getString("postButton.dragAndDropText"));
        tx.setStyle("-fx-font-size: 30px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        tx.setLayoutX(570);
        tx.setLayoutY(450);

        
        Button selectImg = new Button(lang.getString("postButton.selectFromComputerButton"));
        selectImg.setLayoutX(665);
        selectImg.setLayoutY(500);
        selectImg.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        selectImg.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            
            fileChooser.setTitle(lang.getString("postButton.fileChooserTitle"));
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(lang.getString("postButton.imageFilesFilter"), "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter(lang.getString("postButton.allFilesFilter"), "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
              
                PostScene postScene = new PostScene();
                postScene.setPrimaryStage(primaryStage);
                postScene.setUploadScene(uploadScene);

                Scene scene2 = postScene.getPostScene(primaryStage, selectedFile);
                primaryStage.setScene(scene2);
                primaryStage.setTitle("Smart Samadhan");
            }
        });

        uploadPane.getChildren().addAll(backToHome, imageView, tx, selectImg);
        uploadPane.setStyle("-fx-background-color: black;");
        uploadScene = new Scene(uploadPane, 1550, 800);
        return uploadScene;
    }
}