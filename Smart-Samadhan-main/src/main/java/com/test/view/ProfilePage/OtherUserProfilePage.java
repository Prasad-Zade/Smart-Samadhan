package com.test.view.ProfilePage;

import com.test.dao.ProfileDetailData;
import com.test.dao.UserPostDao;
import com.test.model.Post;
import com.test.servies.InitializeFirbase;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.test.view.Home.HomeMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OtherUserProfilePage {

    private final VBox postContentArea = new VBox();
    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(OtherUserProfilePage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(OtherUserProfilePage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createProfileUI(String userIdToDisplay) {
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: black;");

        ProgressIndicator initialLoader = new ProgressIndicator();
        initialLoader.setMaxSize(60, 60);
        StackPane loaderPane = new StackPane(initialLoader);
        loaderPane.setPrefHeight(300);
        mainContainer.getChildren().add(loaderPane);

        Task<Map<String, Object>> fetchUserTask = new Task<>() {
            @Override
            protected Map<String, Object> call() throws Exception {
                return InitializeFirbase.db.collection("users")
                        .document(userIdToDisplay).get().get().getData();
            }
        };

        fetchUserTask.setOnSucceeded(e -> {
            Map<String, Object> userData = fetchUserTask.getValue();
            if (userData != null) {
                VBox profileHeader = buildProfileHeader(userData, userIdToDisplay, mainContainer);
                loadUserPosts((List<String>) userData.getOrDefault("userPosts", new ArrayList<>()));
                mainContainer.getChildren().setAll(profileHeader, postContentArea);
            } else {
                Text notFoundText = new Text("User not found.");
                notFoundText.setStyle("-fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
                mainContainer.getChildren().setAll(notFoundText);
            }
        });

        fetchUserTask.setOnFailed(e -> {
            Text errorText = new Text("Error loading profile.");
            errorText.setStyle("-fx-fill: red; -fx-font-family: '" + FONT_FAMILY + "';");
            mainContainer.getChildren().setAll(errorText);
            e.getSource().getException().printStackTrace();
        });

        new Thread(fetchUserTask).start();
        return mainContainer;
    }

    private VBox buildProfileHeader(Map<String, Object> userData, String userIdToDisplay, VBox mainContainer) {
        String firstName = String.valueOf(userData.get("firstName"));
        String lastName = String.valueOf(userData.get("lastName"));
        String username = String.valueOf(userData.get("userName"));
        String profileImageUrl = String.valueOf(userData.get("imageUrl"));

        Text nameText = new Text(firstName + " " + lastName);
        nameText.setStyle("-fx-font-size: 30; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");

        Text userIdText = new Text("@" + username);
        userIdText.setStyle("-fx-font-size: 20; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");

        ImageView profilePhoto = new ImageView(profileImageUrl);
        profilePhoto.setFitWidth(150);
        profilePhoto.setFitHeight(150);
        profilePhoto.setClip(new Circle(75, 75, 75));

        Button messageButton = new Button("Message");
        messageButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 20; -fx-font-family: '" + FONT_FAMILY + "';");
        messageButton.setOnAction(e -> {
            ChatPage chatPage = new ChatPage();
            VBox chatUI = chatPage.createChatUI(
                    ProfileDetailData.getUID(),
                    firstName + " " + lastName,
                    userIdToDisplay,
                    profileImageUrl
            );
            mainContainer.getChildren().setAll(chatUI);
        });

        VBox header = new VBox(10, profilePhoto, nameText, userIdText, messageButton);
        header.setAlignment(Pos.CENTER);
        header.setMinWidth(800);

        return header;
    }

    private void loadUserPosts(List<String> postIds) {
        ListView<Post> postsListView = new ListView<>();
        postsListView.setMaxWidth(800);
        postsListView.setMaxHeight(500);
        postsListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");
        postsListView.setCellFactory(lv -> new HomeMain().new PostListCell());

        ProgressIndicator postsLoader = new ProgressIndicator();
        postsLoader.setMaxSize(50, 50);

        StackPane contentStack = new StackPane(postsListView, postsLoader);
        postContentArea.getChildren().setAll(contentStack);

        if (postIds == null || postIds.isEmpty()) {
            postsLoader.setVisible(false);
            Label noPostsLabel = new Label("This user has no posts yet.");
            noPostsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "';");
            contentStack.getChildren().add(noPostsLabel);
            return;
        }

        Task<ObservableList<Post>> fetchPostsTask = new Task<>() {
            @Override
            protected ObservableList<Post> call() {
                String loggedInUserId = ProfileDetailData.getUID();
                return UserPostDao.fetchPostsByIds(postIds, loggedInUserId);
            }
        };

        fetchPostsTask.setOnSucceeded(event -> {
            postsLoader.setVisible(false);
            ObservableList<Post> posts = fetchPostsTask.getValue();
            postsListView.setItems(posts);
            postsListView.setPrefHeight(posts.size() * 450);
            Platform.runLater(() -> postsListView.lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0)));
        });

        fetchPostsTask.setOnFailed(event -> {
            postsLoader.setVisible(false);
            Label errorLabel = new Label("Failed to load posts.");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "';");
            contentStack.getChildren().add(errorLabel);
            event.getSource().getException().printStackTrace();
        });

        new Thread(fetchPostsTask).start();
    }
}