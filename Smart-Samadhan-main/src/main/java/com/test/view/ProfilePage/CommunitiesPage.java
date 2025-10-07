package com.test.view.ProfilePage;

import com.test.dao.CommunityDAO;
import com.test.dao.UserDAO;
import com.test.model.Community;
import com.test.view.LoginPage.Login;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommunitiesPage {

    private static final String FONT_FAMILY = "Poppins";
    private static final String DIALOG_STYLE = "-fx-background-color: #1e1e1e;";
    private static final String INPUT_STYLE = "-fx-background-color: #2e2e2e; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 5; -fx-font-size: 14px;";

    private BorderPane borderPane;
    private ListView<Community> communityListView;
    private ObservableList<Community> communityObservableList;
    private final String currentUserId = Login.userEmail;

    public CommunitiesPage(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public VBox getCommunitiesContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10, 20, 10, 20));
        container.setStyle("-fx-background-color: black;");

        Button createCommunityBtn = new Button("+ Create Community");
        createCommunityBtn.setStyle(
            "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; " +
            "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;"
        );
        createCommunityBtn.setOnAction(e -> showCreateCommunityDialog());

        HBox buttonBox = new HBox(createCommunityBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        communityListView = new ListView<>();
        communityObservableList = FXCollections.observableArrayList();
        communityListView.setItems(communityObservableList);
        
        communityListView.setCellFactory(param -> new CommunityListCell(currentUserId, this::loadCommunities, this));
        communityListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");

        communityListView.setOnMouseClicked(event -> {
            Community selectedCommunity = communityListView.getSelectionModel().getSelectedItem();
            if (selectedCommunity != null) {
                if (communityListView.getSelectionModel().getSelectedItem().getMembers().contains(currentUserId)) {
                    CommunityChatPage chatPage = new CommunityChatPage();
                    VBox chatView = chatPage.createCommunityChatUI(selectedCommunity);
                    borderPane.setCenter(chatView);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Access Denied");
                    alert.setHeaderText(null);
                    alert.setContentText("You must be a member to view this community's chat. Send a request to join!");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.setStyle(DIALOG_STYLE);
                    
                    // --- FIXED DIALOG SIZE ---
                    dialogPane.setPrefSize(400, 150);
                    dialogPane.setMinSize(400, 150);
                    dialogPane.setMaxSize(400, 150);

                    dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
                    Node header = dialogPane.lookup(".header-panel");
                    if (header != null) {
                        header.setStyle("-fx-background-color: #1e1e1e;");
                    }
                    alert.showAndWait();
                }
            }
        });

        loadCommunities();

        VBox.setVgrow(communityListView, Priority.ALWAYS);
        container.getChildren().addAll(buttonBox, communityListView);
        return container;
    }

    private void loadCommunities() {
        List<Community> communities = CommunityDAO.getAllCommunities();
        communityObservableList.setAll(communities);
    }

    private void showCreateCommunityDialog() {
        Dialog<Community> dialog = new Dialog<>();
        dialog.setTitle("Create New Community");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle(DIALOG_STYLE);
        
        // --- FIXED DIALOG SIZE ---
        dialogPane.setPrefSize(550, 600);
        dialogPane.setMinSize(550, 600);
        dialogPane.setMaxSize(550, 600);


        dialog.setHeaderText("Enter details for your new community.");

        Platform.runLater(() -> {
            Node header = dialogPane.lookup(".header-panel");
            if (header != null) {
                header.setStyle("-fx-background-color: #1e1e1e;");
                header.lookup(".label").setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "';");
            }
        });

        ButtonType createButtonType = new ButtonType("Create Community", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        Node createButton = dialogPane.lookupButton(createButtonType);
        createButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");

        VBox contentLayout = new VBox(20);
        contentLayout.setPadding(new Insets(20, 20, 10, 20));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(Region.USE_PREF_SIZE);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        TextField communityName = new TextField();
        communityName.setPromptText("e.g., 'Road Community'");
        communityName.setStyle(INPUT_STYLE + " -fx-control-inner-background: #2e2e2e;-fx-prompt-text-fill:white");

        TextArea communityDesc = new TextArea();
        communityDesc.setPromptText("A short description of your community's purpose.");
        communityDesc.setStyle(INPUT_STYLE + " -fx-control-inner-background: #2e2e2e;-fx-prompt-text-fill:white");
        communityDesc.setWrapText(true);

        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-size: 14px;");
        Label descLabel = new Label("Description:");
        descLabel.setTextFill(Color.WHITE);
        descLabel.setStyle("-fx-font-size: 14px;");

        grid.add(nameLabel, 0, 0);
        grid.add(communityName, 1, 0);
        grid.add(descLabel, 0, 1);
        grid.add(communityDesc, 1, 1);

        VBox addMembersSection = new VBox(10);
        Label addMembersHeader = new Label("Add Initial Members (Optional)");
        addMembersHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 15px;");

        HBox inviteBox = new HBox(10);
        TextField memberEmailField = new TextField();
        memberEmailField.setPromptText("Enter user's email");
        memberEmailField.setStyle(INPUT_STYLE);
        HBox.setHgrow(memberEmailField, Priority.ALWAYS);
        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");

        inviteBox.getChildren().addAll(memberEmailField, addButton);

        ObservableList<String> invitedMembers = FXCollections.observableArrayList();
        ListView<String> invitedMembersListView = new ListView<>(invitedMembers);
        invitedMembersListView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e; -fx-font-size: 14px;");
        invitedMembersListView.setPrefHeight(100);

        addMembersSection.getChildren().addAll(addMembersHeader, inviteBox, invitedMembersListView);

        addButton.setOnAction(e -> {
            String email = memberEmailField.getText().trim();
            if (!email.isEmpty() && !email.equals(currentUserId) && !invitedMembers.contains(email)) {
                if (UserDAO.userExists(email)) {
                    invitedMembers.add(email);
                    memberEmailField.clear();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "User with email '" + email + "' not found.");
                    errorAlert.showAndWait();
                }
            }
        });

        contentLayout.getChildren().addAll(grid, new Separator(Orientation.HORIZONTAL), addMembersSection);
        dialog.getDialogPane().setContent(contentLayout);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType && !communityName.getText().trim().isEmpty()) {
                Community newCommunity = new Community(
                    communityName.getText().trim(),
                    communityDesc.getText().trim(),
                    currentUserId
                );
                List<String> allMembers = new ArrayList<>();
                allMembers.add(currentUserId);
                allMembers.addAll(invitedMembers);
                newCommunity.setMembers(allMembers);
                return newCommunity;
            }
            return null;
        });

        Optional<Community> result = dialog.showAndWait();
        result.ifPresent(newCommunity -> {
            CommunityDAO.createCommunity(newCommunity);
            loadCommunities();
        });
    }

    private void showManageRequestsDialog(Community community) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Join Requests");
        dialog.setHeaderText("Accept or decline requests for '" + community.getName() + "'.");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle(DIALOG_STYLE);

        // --- FIXED DIALOG SIZE ---
        dialogPane.setPrefSize(400, 350);
        dialogPane.setMinSize(400, 350);
        dialogPane.setMaxSize(400, 350);

        Node header = dialogPane.lookup(".header-panel");
        if (header != null) {
            header.setStyle("-fx-background-color: #1e1e1e;");
            header.lookup(".label").setStyle("-fx-text-fill: white;");
        }

        ListView<String> pendingMembersListView = new ListView<>();
        pendingMembersListView.setItems(FXCollections.observableArrayList(community.getPendingMembers()));
        pendingMembersListView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e;");

        pendingMembersListView.setCellFactory(param -> new ListCell<String>() {
            private final HBox content = new HBox(10);
            private final Label emailLabel = new Label();
            private final Button acceptButton = new Button("Accept");
            private final Button declineButton = new Button("Decline");
            private final Region spacer = new Region();

            {
                content.setAlignment(Pos.CENTER_LEFT);
                content.setPadding(new Insets(5));
                emailLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                HBox.setHgrow(spacer, Priority.ALWAYS);

                acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
                declineButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");

                content.getChildren().addAll(emailLabel, spacer, acceptButton, declineButton);

                acceptButton.setOnAction(e -> {
                    CommunityDAO.acceptJoinRequest(community.getCommunityId(), getItem());
                    pendingMembersListView.getItems().remove(getItem());
                });

                declineButton.setOnAction(e -> {
                    
                    CommunityDAO.declineJoinRequest(community.getCommunityId(), getItem());
                    
                    pendingMembersListView.getItems().remove(getItem());
                });
            }

            @Override
            protected void updateItem(String email, boolean empty) {
                super.updateItem(email, empty);
                if (empty || email == null) {
                    setGraphic(null);
                } else {
                    emailLabel.setText(email);
                    setGraphic(content);
                }
            }
        });

        dialog.getDialogPane().setContent(pendingMembersListView);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");

        
        dialog.setOnHidden(e -> loadCommunities());
        dialog.showAndWait();
    }

    private static class CommunityListCell extends ListCell<Community> {
        private final String currentUserId;
        private final Runnable refreshCallback;
        private final CommunitiesPage parentPage;

        private final VBox cellContent = new VBox(8);
        private final HBox topBox = new HBox(10);
        private final VBox infoBox = new VBox(5);
        private final HBox actionBox = new HBox();
        private final Label nameLabel = new Label();
        private final Label descLabel = new Label();
        private final Label memberCountLabel = new Label();
        private final Region spacer = new Region();

        public CommunityListCell(String currentUserId, Runnable refreshCallback, CommunitiesPage parentPage) {
            this.currentUserId = currentUserId;
            this.refreshCallback = refreshCallback;
            this.parentPage = parentPage; 

            cellContent.setPadding(new Insets(15));
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "';");
            descLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: '" + FONT_FAMILY + "';");
            descLabel.setWrapText(true);
            memberCountLabel.setStyle("-fx-text-fill: #007bff; -fx-font-size: 11px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;");

            infoBox.getChildren().addAll(nameLabel, memberCountLabel);
            topBox.getChildren().addAll(infoBox, spacer, actionBox);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            topBox.setAlignment(Pos.TOP_LEFT);
            actionBox.setAlignment(Pos.CENTER_RIGHT);

            cellContent.getChildren().addAll(topBox, descLabel);
        }

        @Override
        protected void updateItem(Community community, boolean empty) {
            super.updateItem(community, empty);
            if (empty || community == null) {
                setText(null);
                setGraphic(null);
                setStyle("-fx-background-color: black;");
            } else {
                nameLabel.setText(community.getName());
                descLabel.setText(community.getDescription());
                memberCountLabel.setText("• " + community.getMembers().size() + " Members");
                actionBox.getChildren().clear();

            
                if (currentUserId.equals(community.getCreatorId()) && !community.getPendingMembers().isEmpty()) {
                    Button manageButton = new Button("Manage Requests (" + community.getPendingMembers().size() + ")");
                    manageButton.setStyle("-fx-background-color: #E5C53C; -fx-text-fill: black; -fx-font-size: 10px; -fx-background-radius: 15; -fx-cursor: hand; -fx-font-weight: bold;");
                    manageButton.setOnAction(e -> parentPage.showManageRequestsDialog(community));
                    actionBox.getChildren().add(manageButton);

                } else if (community.getMembers().contains(currentUserId)) {
                    Text memberText = new Text("✓ Member");
                    memberText.setStyle("-fx-fill: #4CAF50; -fx-font-weight: bold;");
                    actionBox.getChildren().add(memberText);

                } else if (community.getPendingMembers().contains(currentUserId)) {
                    Text pendingText = new Text("Request Sent");
                    pendingText.setStyle("-fx-fill: #E5C53C; -fx-font-style: italic;");
                    actionBox.getChildren().add(pendingText);

                } else {
                    Button joinButton = new Button("Request to Join");
                    joinButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 10px; -fx-background-radius: 15; -fx-cursor: hand;");
                    joinButton.setOnAction(e -> {
                        CommunityDAO.requestToJoinCommunity(community.getCommunityId(), currentUserId);
                        refreshCallback.run();
                    });
                    actionBox.getChildren().add(joinButton);
                }

                setGraphic(cellContent);
                setStyle("-fx-background-color: black; -fx-border-color: #2e2e2e; -fx-border-width: 0 0 1 0;");
                setOnMouseEntered(e -> setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #2e2e2e; -fx-border-width: 0 0 1 0; -fx-cursor: hand;"));
                setOnMouseExited(e -> setStyle("-fx-background-color: black; -fx-border-color: #2e2e2e; -fx-border-width: 0 0 1 0;"));
            }
        }
    }
}