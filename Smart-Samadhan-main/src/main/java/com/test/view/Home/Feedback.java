package com.test.view.Home;

import com.test.dao.FeedbackDAO;
import com.test.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Feedback {

    private int selectedRating = 0;
    private static final String FONT_FAMILY = "Poppins";

    private final LanguageManager lang = LanguageManager.getInstance();

    static {
        try {
            Font.loadFont(Feedback.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Feedback.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Parent createFeedbackScene(Runnable back) {
        ImageView feedbackIcon = new ImageView(new Image("assets/Images/document-icon-graphic-ry6hxlmiuy2qzh2g-2.jpg"));
        feedbackIcon.setFitHeight(40);
        feedbackIcon.setFitWidth(40);

        Label feedbackLabel = new Label(lang.getString("feedback.title"));
        feedbackLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-font-family: '" + FONT_FAMILY + "'; -fx-text-fill: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff)");
        HBox heading = new HBox(20, feedbackIcon, feedbackLabel);
        heading.setTranslateX(220);

        String fieldStyle = "-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e; -fx-text-fill: white;";
        String labelStyle = "-fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white";

        Text nameLabel = new Text(lang.getString("feedback.nameLabel"));
        nameLabel.setStyle(labelStyle);
        TextField nameField = new TextField();
        nameField.setStyle(fieldStyle);
        nameField.setMaxWidth(680);

        Text feedbackText = new Text(lang.getString("feedback.feedbackLabel"));
        feedbackText.setStyle(labelStyle);
        TextArea feedbackArea = new TextArea();
        feedbackArea.setStyle(fieldStyle);
        feedbackArea.setMinHeight(100);
        feedbackArea.setMaxWidth(680);
        feedbackArea.setWrapText(true);

        Text ratingLabel = new Text(lang.getString("feedback.ratingLabel"));
        ratingLabel.setStyle(labelStyle);

        HBox starBox = new HBox(10);
        starBox.setAlignment(Pos.CENTER_LEFT);
        Image filledStar = new Image("assets/Images/star-filled-fivepointed-shape.png");
        Image emptyStar = new Image("assets/Images/star-filled-fivepointed-shape (1).png");

        ImageView[] stars = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            ImageView star = new ImageView(emptyStar);
            star.setFitWidth(32);
            star.setFitHeight(32);

            star.setOnMouseEntered(e -> updateStars(stars, rating, filledStar, emptyStar));
            star.setOnMouseExited(e -> updateStars(stars, selectedRating, filledStar, emptyStar));
            star.setOnMouseClicked(e -> {
                selectedRating = rating;
                updateStars(stars, rating, filledStar, emptyStar);
            });

            stars[i] = star;
            starBox.getChildren().add(star);
        }

        Button backBtn = new Button(lang.getString("feedback.backButton"));
        Button submitBtn = new Button(lang.getString("feedback.submitButton"));

        backBtn.setOnAction(e -> back.run());
        submitBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String feedbackMsg = feedbackArea.getText().trim();

            if (name.isEmpty() || selectedRating == 0) {
                showFancyAlert("alert.missingFields.title", "alert.missingFields.message", true);
            } else {
                FeedbackDAO.submitFeedback(name, feedbackMsg, selectedRating);
                showFancyAlert("alert.feedbackSubmitted.title", "alert.feedbackSubmitted.message", false);
                nameField.clear();
                feedbackArea.clear();
                selectedRating = 0;
                updateStars(stars, selectedRating, filledStar, emptyStar);

                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    javafx.application.Platform.runLater(back);
                }).start();
            }
        });

        String buttonStyle = "-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);";
        backBtn.setStyle(buttonStyle);
        submitBtn.setStyle(buttonStyle);
        backBtn.setMinSize(120, 40);
        submitBtn.setMinSize(120, 40);

        HBox buttonRow = new HBox(20, backBtn, submitBtn);
        buttonRow.setTranslateX(230);

        VBox formLayout = new VBox(15,
                nameLabel, nameField,
                feedbackText, feedbackArea,
                ratingLabel, starBox
        );
        formLayout.setPadding(new Insets(0, 20, 0, 20));

        VBox mainLayout = new VBox(25, heading, formLayout, buttonRow);
        mainLayout.setPadding(new Insets(30, 0, 30, 120));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        VBox container = new VBox(mainLayout);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: transparent;");
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private void updateStars(ImageView[] stars, int rating, Image filledStar, Image emptyStar) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImage(i < rating ? filledStar : emptyStar);
        }
    }

    private void showFancyAlert(String titleKey, String messageKey, boolean isError) {
        Stage alertStage = new Stage();
        alertStage.initStyle(StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(lang.getString(titleKey));
        Text messageText = new Text(lang.getString(messageKey));

        String baseTitleStyle = "-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;";
        if (isError) {
            titleText.setStyle(baseTitleStyle + "-fx-fill: red;");
        } else {
            titleText.setStyle(baseTitleStyle + "-fx-fill: green;");
        }

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