package com.test.view.Post;

import com.test.Controller.PostSceneController;
import com.test.dao.PostDAO;
import com.test.dao.ProfileDetailData;
import com.test.util.LanguageManager;
import com.test.view.Home.HomeMain;
import com.test.view.LoginPage.Login;
import com.test.view.ReportIssue.ReportIssue;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;

class PostScene {
    private Stage primaryStage;
    private Scene uploadScene;
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(PostScene.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(PostScene.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUploadScene(Scene postButtonScene) {
        uploadScene = postButtonScene;
    }

    public Scene getPostScene(Stage stage, File imageFile) {
        this.primaryStage = stage;
        Image image = new Image(imageFile.toURI().toString());

        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("English", "Marathi", "Hindi", "Gujarati","Telgu","Russian","Chinese","Spanish","French");
        languageCombo.setPromptText(lang.getString("postScene.translatePrompt"));
        languageCombo.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);" +
            "-fx-mark-color: white;"
        );

        languageCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? lang.getString("postScene.translatePrompt") : item);
                setTextFill(Color.WHITE);
            }
        });

        ImageView previewImage = new ImageView(image);
        previewImage.setFitWidth(800);
        previewImage.setFitHeight(450);
        previewImage.setPreserveRatio(true);
        previewImage.setTranslateY(70);
        
        

        Image profile = new Image(ProfileDetailData.getImgProfile());
        ImageView imageView = new ImageView(profile);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setClip(new Circle(35, 35, 35)); // Corrected clip for a 70px image

        Text userName = new Text(ProfileDetailData.getUserName());
        userName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        

        Text Report = new Text(lang.getString("postScene.reportLink"));
        Report.setStyle("-fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: red; -fx-cursor: hand;");
       

        Report.setOnMouseClicked(e -> {
            ReportIssue reportIssue = new ReportIssue();
            Scene reportScene = reportIssue.createReportScene(() -> {
                PostScene postScene = new PostScene();
                postScene.setPrimaryStage(primaryStage);
                postScene.setUploadScene(uploadScene);
                Scene postSceneBack = postScene.getPostScene(primaryStage, imageFile);
                primaryStage.setScene(postSceneBack);
                primaryStage.setTitle("");
            }, imageFile, Login.userEmail);
            primaryStage.setScene(reportScene);
        });
        
   
        Region userSpacer = new Region();
        HBox.setHgrow(userSpacer, Priority.ALWAYS);

       
        HBox user = new HBox(15, imageView, userName, userSpacer, Report);
        user.setAlignment(Pos.CENTER_LEFT); 
        


        Text location = new Text(lang.getString("postScene.locationLabel"));
        location.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextArea locationTextArea = new TextArea();
        locationTextArea.setWrapText(true);
        locationTextArea.setFocusTraversable(false);
        locationTextArea.setMaxWidth(370);
        locationTextArea.setMaxHeight(90);
        locationTextArea.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-control-inner-background: #2e2e2e;");

        Text description = new Text(lang.getString("postScene.descriptionLabel"));
        description.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setFocusTraversable(false);
        descriptionTextArea.setMaxWidth(370);
        descriptionTextArea.setMaxHeight(200);
        descriptionTextArea.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-control-inner-background: #2e2e2e;");

        Button aiBtn = new Button(lang.getString("postScene.aiButton"));
        aiBtn.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        StackPane descriptionPane = new StackPane(descriptionTextArea);
        StackPane locationPane = new StackPane(locationTextArea);

        Rectangle descriptionClip = new Rectangle();
        descriptionClip.widthProperty().bind(descriptionPane.widthProperty());
        descriptionClip.heightProperty().bind(descriptionPane.heightProperty());
        descriptionPane.setClip(descriptionClip);

        Rectangle locationClip = new Rectangle();
        locationClip.widthProperty().bind(locationPane.widthProperty());
        locationClip.heightProperty().bind(locationPane.heightProperty());
        locationPane.setClip(locationClip);

        aiBtn.setOnAction(arg0 -> {
            Rectangle shimmer = createShimmerRectangle(descriptionTextArea);
            descriptionPane.getChildren().add(shimmer);
            TranslateTransition animation = createShimmerAnimation(shimmer, descriptionTextArea.getMaxWidth());
            animation.play();

            Task<String> aiTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    String locationText = locationTextArea.getText();
                    String descriptionText = descriptionTextArea.getText();
                    PostSceneController controller = new PostSceneController();
                    return controller.aiPrompString(locationText, descriptionText, null);
                }
            };

            aiTask.setOnSucceeded(event -> {
                descriptionTextArea.setText(aiTask.getValue());
                animation.stop();
                descriptionPane.getChildren().remove(shimmer);
            });

            aiTask.setOnFailed(event -> {
                animation.stop();
                descriptionPane.getChildren().remove(shimmer);
                System.err.println("AI task failed: " + aiTask.getException().getMessage());
            });

            new Thread(aiTask).start();
        });

        Button languageButton = new Button(lang.getString("postScene.translateButton"));
        languageButton.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        languageButton.setOnAction(arg0 -> {
            String selected = languageCombo.getValue();
            if (selected != null) {

                Rectangle descShimmer = createShimmerRectangle(descriptionTextArea);
                descriptionPane.getChildren().add(descShimmer);
                TranslateTransition descAnimation = createShimmerAnimation(descShimmer, descriptionTextArea.getMaxWidth());
                descAnimation.play();

                Task<String[]> translateTask = new Task<>() {
                    @Override
                    protected String[] call() throws Exception {
                        String descriptionText = descriptionTextArea.getText();
                        PostSceneController controller = new PostSceneController();
                        String translatedDescription = controller.translateText(descriptionText, selected);
                        return new String[]{translatedDescription};
                    }
                };

                translateTask.setOnSucceeded(event -> {
                    String[] translations = translateTask.getValue();
                    descriptionTextArea.setText(translations[0]);
                    descAnimation.stop();
                    descriptionPane.getChildren().remove(descShimmer);
                });

                translateTask.setOnFailed(event -> {
                    descAnimation.stop();
                    descriptionPane.getChildren().remove(descShimmer);
                    System.err.println("Translation task failed: " + translateTask.getException().getMessage());
                });

                new Thread(translateTask).start();
            } else {
                showFancyAlert("alert.noLanguageSelected.title", "alert.noLanguageSelected.message");
            }
        });

        Button backBtn = new Button(lang.getString("postScene.backButton"));
        backBtn.setMinWidth(80);
        backBtn.setMinHeight(30);
        backBtn.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        backBtn.setOnAction(arg0 -> {
            primaryStage.setScene(uploadScene);
            primaryStage.setTitle(lang.getString("postScene.uploadTitle"));
        });

        Button postBtn = new Button(lang.getString("postScene.postButton"));
        postBtn.setMinWidth(80);
        postBtn.setMinHeight(30);
        postBtn.setStyle("-fx-background-color: #ff3b30; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 15;");
        postBtn.setOnAction(arg0 -> {
            String locationText = locationTextArea.getText().trim();
            String descriptionText = descriptionTextArea.getText().trim();

            if (locationText.isEmpty() || descriptionText.isEmpty()) {
                showFancyAlert("alert.missingPostFields.title", "alert.missingPostFields.message");
                return;
            }

            PostDAO postDao = new PostDAO();
            String userEmail = Login.userEmail;
            postDao.createPost(imageFile, locationText, descriptionText, userEmail);

            HomeMain homeMain = new HomeMain();
            homeMain.setStage(primaryStage);
            Scene homeScene = homeMain.getHomeScene(primaryStage);
            primaryStage.setScene(homeScene);
            primaryStage.setTitle(lang.getString("Smart Samadhan"));
        });

        HBox locationBox = new HBox(30, location, locationPane);
        HBox discriptionBox = new HBox(10, description, descriptionPane);
        
        Region aiSpacer = new Region();
        HBox.setHgrow(aiSpacer, Priority.ALWAYS);
        HBox languageBox = new HBox(10, languageCombo, languageButton, aiSpacer, aiBtn);
        languageBox.setAlignment(Pos.CENTER_LEFT);

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);
        HBox nextback = new HBox(backBtn, bottomSpacer, postBtn);

        VBox vb1 = new VBox(20, user, locationBox, discriptionBox, languageBox, nextback);
        vb1.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 50;");
        vb1.setPadding(new Insets(20, 20, 40, 20));
        vb1.setLayoutX(950);
        vb1.setLayoutY(100);

        Pane main = new Pane();
        main.setStyle("-fx-background-color: black");

        previewImage.setLayoutX(100);
        previewImage.setLayoutY(90);

        main.getChildren().addAll(previewImage, vb1);

        return new Scene(main, 1550, 800);
    }

    private Rectangle createShimmerRectangle(Region nodeToCover) {
        Rectangle shimmerRect = new Rectangle();
        shimmerRect.widthProperty().bind(nodeToCover.widthProperty());
        shimmerRect.heightProperty().bind(nodeToCover.heightProperty());

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.TRANSPARENT),
            new Stop(0.4, Color.rgb(255, 255, 255, 0.15)),
            new Stop(0.5, Color.rgb(255, 255, 255, 0.3)),
            new Stop(0.6, Color.rgb(255, 255, 255, 0.15)),
            new Stop(1, Color.TRANSPARENT)
        );
        shimmerRect.setFill(gradient);
        return shimmerRect;
    }

    private TranslateTransition createShimmerAnimation(Rectangle shimmer, double width) {
        TranslateTransition shimmerAnimation = new TranslateTransition(Duration.seconds(1.6), shimmer);
        shimmerAnimation.setFromX(-width);
        shimmerAnimation.setToX(width);
        shimmerAnimation.setCycleCount(Animation.INDEFINITE);
        return shimmerAnimation;
    }

    private void showFancyAlert(String titleKey, String messageKey) {
        Stage alertStage = new Stage();
        alertStage.initStyle(StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(lang.getString(titleKey));
        titleText.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: red; -fx-font-weight: bold;");

        Text messageText = new Text(lang.getString(messageKey));
        messageText.setStyle("-fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");

        Button closeButton = new Button(lang.getString("alert.okButton"));
        closeButton.setStyle(
            "-fx-background-radius: 15;" +
            "-fx-font-size: 13px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-text-fill: white;" +
            "-fx-padding: 5 20 5 20;" +
            "-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff);"
        );
        closeButton.setOnAction(e -> alertStage.close());

        VBox vbox = new VBox(10, titleText, messageText, closeButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 20; -fx-padding: 20;");
        Scene scene = new Scene(vbox);
        scene.setFill(Color.TRANSPARENT);

        alertStage.setScene(scene);
        alertStage.show();
    }
}