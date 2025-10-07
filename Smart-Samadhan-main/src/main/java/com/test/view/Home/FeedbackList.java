package com.test.view.Home;

import com.test.dao.FeedbackDAO;
import com.test.model.FeedbackModel;
import com.test.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class FeedbackList {

    private static final String FONT_FAMILY = "Poppins";

    private final LanguageManager lang = LanguageManager.getInstance();

    static {
        try {
            Font.loadFont(FeedbackList.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(FeedbackList.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Parent createFeedbackListScene(Runnable backAction) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);");

        Label heading = new Label(lang.getString("feedbackList.title"));
        heading.setStyle("-fx-font-size: 22px; -fx-text-fill: linear-gradient(to right, #00c6ff, #0072ff); -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");
        heading.setTranslateX(400);

        VBox feedbackList = new VBox(15);
        feedbackList.setPadding(new Insets(10));

        List<FeedbackModel> feedbacks = FeedbackDAO.getAllFeedbacks();
        for (FeedbackModel fb : feedbacks) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #2c2c2e; -fx-background-radius: 10;");

            Text name = new Text(lang.getString("feedbackList.nameLabel") + " " + fb.getName());
            name.setStyle("-fx-fill: white; -fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "';");

            Text feedbackText = new Text(lang.getString("feedbackList.feedbackLabel") + " " + fb.getFeedback());
            feedbackText.setStyle("-fx-fill: white; -fx-font-size: 13; -fx-font-family: '" + FONT_FAMILY + "';");
            feedbackText.setWrappingWidth(800);

            HBox starRow = new HBox(5);
            for (int i = 0; i < fb.getRating(); i++) {
                ImageView star = new ImageView("assets/Images/star-filled-fivepointed-shape.png");
                star.setFitWidth(18);
                star.setFitHeight(18);
                starRow.getChildren().add(star);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            Text time = new Text(lang.getString("feedbackList.submittedOnLabel") + " " + sdf.format(fb.getTimestamp()));
            time.setStyle("-fx-fill: gray; -fx-font-size: 11; -fx-font-family: '" + FONT_FAMILY + "';");

            card.getChildren().addAll(name, feedbackText, starRow, time);
            feedbackList.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(feedbackList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: linear-gradient(to bottom, #1b2735, #090a0f); -fx-border-color: transparent;");
        scrollPane.setPrefHeight(500);

        Button backButton = new Button(lang.getString("feedbackList.backButton"));
        backButton.setStyle("-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);");
        backButton.setMinSize(120, 40);
        backButton.setOnAction(e -> backAction.run());

        root.getChildren().addAll(heading, scrollPane, backButton);
        root.setStyle("-fx-background-radius: 25;");
        return root;
    }
}