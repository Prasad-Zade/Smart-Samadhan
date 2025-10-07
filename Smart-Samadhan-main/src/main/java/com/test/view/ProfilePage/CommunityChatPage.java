package com.test.view.ProfilePage;

import com.test.dao.CommunityDAO;
import com.test.dao.CommunityMessageDAO;
import com.test.dao.UserDAO;
import com.test.model.Community;
import com.test.model.CommunityMessageModel;
import com.test.view.LoginPage.Login;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CommunityChatPage {
    private static final String FONT_FAMILY = "Poppins";
    private static final String DIALOG_STYLE = "-fx-background-color: #1e1e1e;";
    private static final String INPUT_STYLE = "-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 5;";

    private VBox messageContainer;
    private ScrollPane scrollPane;
    private Timeline messageRefreshTimeline;
    private String currentCommunityId;
    private String currentUserId;
    private int lastMessageCount = 0;
    
    public VBox createCommunityChatUI(Community community) {
        currentUserId = Login.userEmail;
        currentCommunityId = community.getCommunityId();
        
        VBox root = new VBox();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #090a0f; -fx-border-color: #333; -fx-border-width: 0 0 1 0;");

        Text communityNameText = new Text(community.getName());
        communityNameText.setStyle("-fx-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(communityNameText, spacer);
        
        if (currentUserId.equals(community.getCreatorId())) {
            Button manageMembersBtn = new Button("Manage Members");
            manageMembersBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 15; -fx-cursor: hand;");
            manageMembersBtn.setOnAction(e -> showManageMembersDialog(community));
            headerBox.getChildren().add(manageMembersBtn);
        }

        messageContainer = new VBox(10);
        messageContainer.setPadding(new Insets(15));
        
        scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        loadAndDisplayMessages(currentUserId, community.getCommunityId());
        startMessageRefresh();

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message in " + community.getName());
        inputField.setStyle("-fx-background-radius: 20; -fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-padding: 12; -fx-font-family: '" + FONT_FAMILY + "';");
        inputField.setPrefHeight(45);

        Button sendButton = new Button("➤");
        sendButton.setStyle("-fx-background-color: #007AFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 50%; -fx-cursor: hand;");
        sendButton.setPrefSize(45, 45);
        
        inputField.setOnAction(e -> sendMessage(inputField, community.getCommunityId()));
        sendButton.setOnAction(e -> sendMessage(inputField, community.getCommunityId()));

        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setStyle("-fx-background-color: #090a0f;");

        root.getChildren().addAll(headerBox, scrollPane, inputBox);
        return root;
    }

    private void showManageMembersDialog(Community community) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Manage Members for '" + community.getName() + "'");
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle(DIALOG_STYLE);
        dialogPane.setPrefWidth(550);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");
        
        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(20));
        
        Runnable refreshDialogCallback = () -> {
            dialog.close();
            List<Community> allCommunities = CommunityDAO.getAllCommunities();
            allCommunities.stream()
                .filter(c -> c.getCommunityId().equals(community.getCommunityId()))
                .findFirst()
                .ifPresent(this::showManageMembersDialog);
        };
        
        // --- Pending Requests Section ---
        if (!community.getPendingMembers().isEmpty()) {
            VBox pendingSection = new VBox(10);
            Label pendingHeader = new Label("Pending Join Requests (" + community.getPendingMembers().size() + ")");
            pendingHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");
            ListView<String> pendingListView = new ListView<>();
            pendingListView.getItems().setAll(community.getPendingMembers());
            pendingListView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e;");
            pendingListView.setCellFactory(param -> new PendingMemberCell(community.getCommunityId(), refreshDialogCallback));
            pendingSection.getChildren().addAll(pendingHeader, pendingListView);
            dialogContent.getChildren().add(pendingSection);
        }

        // --- Current Members Section ---
        VBox membersSection = new VBox(10);
        Label membersHeader = new Label("Current Members (" + community.getMembers().size() + ")");
        membersHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");
        ListView<String> membersListView = new ListView<>();
        membersListView.getItems().setAll(community.getMembers());
        membersListView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e;");
        membersListView.setCellFactory(param -> new CurrentMemberCell(community.getCommunityId(), community.getCreatorId(), refreshDialogCallback));
        
        membersSection.getChildren().addAll(membersHeader, membersListView);
        
        // --- Invite New Member Section ---
        VBox inviteSection = new VBox(10);
        Label inviteHeader = new Label("Invite New Member");
        inviteHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");
        
        HBox inviteBox = new HBox(10);
        TextField inviteEmailField = new TextField();
        inviteEmailField.setPromptText("Enter user's email to invite");
        inviteEmailField.setStyle(INPUT_STYLE);
        HBox.setHgrow(inviteEmailField, Priority.ALWAYS);
        Button inviteButton = new Button("Invite");
        inviteButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        
        inviteButton.setOnAction(e -> {
            String email = inviteEmailField.getText().trim();
            if (!email.isEmpty()) {
                if (UserDAO.userExists(email)) {
                    if (!community.getMembers().contains(email)) {
                        CommunityDAO.inviteUserToCommunity(community.getCommunityId(), email);
                        refreshDialogCallback.run();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "User is already a member.").showAndWait();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "User with email '" + email + "' not found.").showAndWait();
                }
            }
        });
        
        inviteBox.getChildren().addAll(inviteEmailField, inviteButton);
        inviteSection.getChildren().addAll(inviteHeader, inviteBox);
        
        dialogContent.getChildren().addAll(membersSection, new Separator(Orientation.HORIZONTAL), inviteSection);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.showAndWait();
    }

    private static class CurrentMemberCell extends ListCell<String> {
        private final String communityId;
        private final String adminId;
        private final Runnable refreshCallback;
        private final HBox contentBox = new HBox(15);
        private final Label memberInfoLabel = new Label();
        private final Button removeButton = new Button("Remove");

        public CurrentMemberCell(String communityId, String adminId, Runnable refreshCallback) {
            this.communityId = communityId;
            this.adminId = adminId;
            this.refreshCallback = refreshCallback;

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
            memberInfoLabel.setTextFill(Color.WHITE);
            memberInfoLabel.setStyle("-fx-font-size: 14px;");

            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setPadding(new Insets(5, 10, 5, 10));
            contentBox.getChildren().addAll(memberInfoLabel, spacer, removeButton);
        }

        @Override
        protected void updateItem(String userId, boolean empty) {
            super.updateItem(userId, empty);
            if (empty || userId == null) {
                setGraphic(null);
                setStyle("-fx-background-color: #2e2e2e;");
            } else {
                String displayName = UserDAO.getUserDisplayName(userId);
                memberInfoLabel.setText(displayName + (userId.equals(adminId) ? " (Admin)" : ""));
                

                removeButton.setVisible(!userId.equals(adminId));
                
                removeButton.setOnAction(e -> {
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " + displayName + "?");
                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        CommunityDAO.removeMemberFromCommunity(communityId, userId);
                        refreshCallback.run();
                    }
                });
                
                setGraphic(contentBox);
                setStyle("-fx-background-color: #2e2e2e;");
            }
        }
    }
    
    private static class PendingMemberCell extends ListCell<String> {
        private final String communityId;
        private final Runnable refreshCallback;
        private final HBox contentBox = new HBox(15);
        private final Label memberIdLabel = new Label();
        private final Button acceptButton = new Button("Accept");
        private final Button declineButton = new Button("Decline");
        
        public PendingMemberCell(String communityId, Runnable refreshCallback) {
            this.communityId = communityId;
            this.refreshCallback = refreshCallback;
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
            declineButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
            memberIdLabel.setTextFill(Color.WHITE);
            memberIdLabel.setStyle("-fx-font-size: 14px;");

            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setPadding(new Insets(5, 10, 5, 10));
            contentBox.getChildren().addAll(memberIdLabel, spacer, acceptButton, declineButton);
        }

        @Override
        protected void updateItem(String userId, boolean empty) {
            super.updateItem(userId, empty);
            if (empty || userId == null) {
                setGraphic(null);
                setStyle("-fx-background-color: #2e2e2e;");
            } else {
                String displayName = UserDAO.getUserDisplayName(userId);
                memberIdLabel.setText(displayName);
                
                acceptButton.setOnAction(e -> {
                    CommunityDAO.acceptJoinRequest(communityId, userId);
                    refreshCallback.run();
                });
                
                declineButton.setOnAction(e -> {
                    CommunityDAO.declineJoinRequest(communityId, userId);
                    refreshCallback.run();
                });
                
                setGraphic(contentBox);
                setStyle("-fx-background-color: #2e2e2e;");
            }
        }
    }
    
    private void sendMessage(TextField inputField, String communityId) {
        String msgText = inputField.getText().trim();
        if (!msgText.isEmpty()) {
            String senderName = UserDAO.getUserDisplayName(currentUserId);
            CommunityMessageDAO.sendCommunityMessage(communityId, currentUserId, senderName, msgText);
            inputField.clear();
            Platform.runLater(() -> loadAndDisplayMessages(currentUserId, communityId));
        }
    }
    
    private void startMessageRefresh() {
        if (messageRefreshTimeline != null) {
            messageRefreshTimeline.stop();
        }
        messageRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Platform.runLater(() -> {
                int currentMessageCount = CommunityMessageDAO.getCommunityMessageCount(currentCommunityId);
                if (currentMessageCount != lastMessageCount) {
                    loadAndDisplayMessages(currentUserId, currentCommunityId);
                }
            });
        }));
        messageRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        messageRefreshTimeline.play();
    }
    
    public void stopMessageRefresh() {
        if (messageRefreshTimeline != null) {
            messageRefreshTimeline.stop();
        }
    }
    
    private void loadAndDisplayMessages(String currentUserId, String communityId) {
        List<CommunityMessageModel> messages = CommunityMessageDAO.getCommunityMessages(communityId);
        messageContainer.getChildren().clear();
        for (CommunityMessageModel msg : messages) {
            boolean isSender = msg.getSenderId().equals(currentUserId);
            HBox messageBubble = createMessageBubble(msg.getSenderName(), msg.getMessage(), msg.getTimestamp(), isSender);
            messageContainer.getChildren().add(messageBubble);
        }
        lastMessageCount = messages.size();
        Platform.runLater(() -> {
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }
    
    private HBox createMessageBubble(String senderName, String message, long timestamp, boolean isCurrentUserTheSender) {
        VBox bubbleContainer = new VBox(3);
        bubbleContainer.setMaxWidth(450);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(10, 15, 10, 15));
        
        String bubbleStyle = "-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "';";
        
        HBox wrapper = new HBox();
        if (isCurrentUserTheSender) {
            bubbleStyle += "-fx-background-color: #007AFF;";
            wrapper.setAlignment(Pos.CENTER_RIGHT);
        } else {
            bubbleStyle += "-fx-background-color: #3A3A3C;";
            wrapper.setAlignment(Pos.CENTER_LEFT);
        }
        messageLabel.setStyle(bubbleStyle);

        String timeString = new SimpleDateFormat("HH:mm").format(new Date(timestamp));
        Label timeLabel = new Label(timeString);
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #8E8E93; -fx-padding: 2 8 0 8;");
        
        if (!isCurrentUserTheSender) {
            Label senderLabel = new Label(senderName);
            senderLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #00c6ff; -fx-font-weight: bold; -fx-padding: 0 8 0 8;");
            bubbleContainer.getChildren().add(senderLabel);
        }
        
        bubbleContainer.getChildren().addAll(messageLabel, timeLabel);
        wrapper.getChildren().add(bubbleContainer);
        return wrapper;
    }
}