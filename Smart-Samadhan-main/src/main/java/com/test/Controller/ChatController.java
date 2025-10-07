package com.test.Controller;

import com.test.model.ChatModel;
import com.test.model.MessageModel;
import com.test.view.LoginPage.Login;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {
    String email=Login.userEmail;
    private final ChatModel model;
    private final ListView<MessageModel> chatListView;
    private final TextField inputField;

    public ChatController(ListView<MessageModel> chatListView, TextField inputField) {
        this.model = new ChatModel();
        this.chatListView = chatListView;
        this.inputField = inputField;
    }

    public void handleSend() {
        String userText = inputField.getText().trim();
        if (!userText.isEmpty()) {
            chatListView.getItems().add(new MessageModel(userText, true));

            String response = model.getResponse(userText,email);
            chatListView.getItems().add(new MessageModel(response, false));

            chatListView.scrollTo(chatListView.getItems().size() - 1); // Optional: auto scroll
            inputField.clear();
        }
    }

    public void addBotMessage(String message) {
        MessageModel botMessage = new MessageModel(message, false);
        chatListView.getItems().add(botMessage);
    }
}
