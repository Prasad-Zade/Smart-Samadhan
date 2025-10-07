package com.test.view.ProfilePage;

import com.test.dao.MessageDAO;
import com.test.model.MessageModel;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatPage {

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(ChatPage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ChatPage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createChatUI(String senderId, String receiverName, String receiverId, String receiverProfileUrl) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f)");

        ImageView profileImage = new ImageView();
        profileImage.setFitWidth(40);
        profileImage.setFitHeight(40);

        try {
            Image img = new Image(receiverProfileUrl, true);
            profileImage.setImage(img);
            profileImage.setClip(new Circle(20, 20, 20));
        } catch (Exception e) {
            System.out.println("Profile image load error.");
        }

        Text receiverText = new Text(receiverName);
        receiverText.setStyle("-fx-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "';");

        headerBox.getChildren().addAll(profileImage, receiverText);

        VBox messageContainer = new VBox(10);
        messageContainer.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: linear-gradient(to bottom, #1b2735, #090a0f); -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinHeight(500);

        List<MessageModel> messages = MessageDAO.getAllMessages(senderId, receiverId);
        for (MessageModel msg : messages) {
            boolean isSender = msg.getSenderId().equals(senderId);
            HBox messageBubble = createMessageBubble(msg.getMessage(), msg.getTimestamp(), isSender);
            messageContainer.getChildren().add(messageBubble);
        }

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setStyle("-fx-background-radius: 30; -fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-text-fill: white; -fx-border-color: #444; -fx-border-radius: 30; -fx-padding: 15; -fx-font-family: '" + FONT_FAMILY + "';");
        inputField.setPrefHeight(50);
        inputField.setMinWidth(930);

        Button sendButton = new Button("➤");
        sendButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 50%;");
        sendButton.setPrefSize(50, 50);

        sendButton.setOnAction(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                String time = new SimpleDateFormat("HH:mm").format(new Date());
                MessageDAO.sendMessage(senderId, receiverId, msg);
                HBox newMsg = createMessageBubble(msg, time, true);
                messageContainer.getChildren().add(newMsg);
                inputField.clear();

                scrollPane.layout();
                scrollPane.setVvalue(1.0);
            }
        });

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);");

        root.getChildren().addAll(headerBox, scrollPane, inputBox);
        return root;
    }

    public HBox createMessageBubble(String message, Object timeObj, boolean isSender) {
        VBox bubbleContainer = new VBox(3);
        bubbleContainer.setMaxWidth(400);
        bubbleContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(15, 20, 15, 20));
        messageLabel.setMaxWidth(350);
        messageLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

        String bubbleStyle = "-fx-background-radius: 25; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.5, 0, 2);";
        if (isSender) {
            bubbleStyle += "-fx-background-color: linear-gradient(to bottom right, #007AFF, #0056D6, #004BB8);";
        } else {
            bubbleStyle += "-fx-background-color: linear-gradient(to bottom right, #3A3A3C, #2C2C2E);";
        }
        messageLabel.setStyle(bubbleStyle);

        String timeString;
        if (timeObj instanceof String) {
            timeString = (String) timeObj;
        } else if (timeObj instanceof Long) {
            timeString = new SimpleDateFormat("HH:mm").format(new Date((Long) timeObj));
        } else {
            timeString = new SimpleDateFormat("HH:mm").format(new Date());
        }

        Label timeLabel = new Label(timeString);
        timeLabel.setStyle("-fx-font-size: 10px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-text-fill: #8E8E93; -fx-padding: 2 8 0 8;");

        bubbleContainer.getChildren().addAll(messageLabel, timeLabel);

        HBox wrapper = new HBox();
        wrapper.setPadding(new Insets(4, 15, 4, 15));
        wrapper.setMaxWidth(Region.USE_COMPUTED_SIZE);

        if (isSender) {
            wrapper.setAlignment(Pos.CENTER_RIGHT);
            bubbleContainer.setAlignment(Pos.CENTER_RIGHT);
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
        } else {
            wrapper.setAlignment(Pos.CENTER_LEFT);
            bubbleContainer.setAlignment(Pos.CENTER_LEFT);
            timeLabel.setAlignment(Pos.CENTER_LEFT);
        }

        wrapper.getChildren().add(bubbleContainer);
        return wrapper;
    }

    public HBox createMessageBubble(String message, String time, boolean isSender) {
        return createMessageBubble(message, (Object) time, isSender);
    }

    public HBox createMessageBubble(String message, long time, boolean isSender) {
        return createMessageBubble(message, (Object) time, isSender);
    }
}