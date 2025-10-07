package com.test.view.ChatBot;

import com.test.Controller.ChatController;
import com.test.dao.ProfileDetailData;
import com.test.model.MessageModel;
import com.test.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ChatView {
    private ListView<MessageModel> chatListView;
    private TextField inputField;
    private ChatController controller;
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(ChatView.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ChatView.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Parent getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        Label title = new Label(lang.getString("chat.title"));
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';-fx-fill: #007bff;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        chatListView = new ListView<>();
        chatListView.setStyle("-fx-control-inner-background: transparent; -fx-background-color: transparent;");
        chatListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(MessageModel msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(msg.getContent());
                    text.setWrappingWidth(400);
                    text.setStyle("-fx-font-size: 16px; -fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

                    HBox bubble = new HBox(text);
                    bubble.setPadding(new Insets(15));
                    bubble.setMaxWidth(400);
                    bubble.setStyle(msg.isUser()
                            ? "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 15;"
                            : "-fx-background-color: #3a3a3a; -fx-background-radius: 15;");

                    setStyle("-fx-background-color: transparent;");
                    if (msg.isUser()) {
                        setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        setAlignment(Pos.CENTER_LEFT);
                    }
                    setGraphic(bubble);
                }
            }
        });

        chatListView.setPrefHeight(600);

        inputField = new TextField();
        inputField.setPromptText(lang.getString("chat.prompt"));
        inputField.setStyle("-fx-background-radius: 10; -fx-font-family: '" + FONT_FAMILY + "';");
        inputField.setMinWidth(900);
        inputField.setMinHeight(40);

        Button sendButton = new Button(lang.getString("chat.sendButton"));
        sendButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "';");
        sendButton.setDefaultButton(true);
        sendButton.setMinHeight(40);
        sendButton.setMinWidth(60);

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER);

        controller = new ChatController(chatListView, inputField);
        
        String greetingFormat = lang.getString("chat.greeting");
        String timeOfDay = lang.getString(getTimeOfDayKey());
        String greeting = String.format(greetingFormat, timeOfDay, ProfileDetailData.firstName);

        controller.addBotMessage(greeting);
        sendButton.setOnAction(e -> controller.handleSend());
        inputField.setOnAction(e -> controller.handleSend());

        root.setMinHeight(600);
        root.setMinWidth(700);
        root.getChildren().addAll(title, chatListView, inputBox);
        return root;
    }

    private String getTimeOfDayKey() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) return "time.morning";
        if (hour >= 12 && hour < 17) return "time.afternoon";
        if (hour >= 17 && hour < 21) return "time.evening";
        return "time.night";
    }
}