package com.test.view.ProfilePage;

import com.test.dao.EditProfileData;
import com.test.dao.ProfileDetailData;
import com.test.dao.UserPostDao;
import com.test.model.Post;
import com.test.view.Home.HomeMain;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class ProfilePage {

    private Stage primaryStage;
    private BorderPane bp;
    private VBox postContentArea;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(ProfilePage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ProfilePage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createProfileUI(Stage myProfileStage, BorderPane bp) {
        this.bp = bp;
        this.primaryStage = myProfileStage;

        Text userName = new Text(ProfileDetailData.getFirstName() + " " + ProfileDetailData.getLastName());
        userName.setStyle("-fx-font-size: 24; -fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

        Text userId = new Text("@" + ProfileDetailData.getUserName());
        userId.setStyle("-fx-font-size: 16; -fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

        Text location = new Text(ProfileDetailData.getLocation());
        location.setStyle("-fx-font-size:14; -fx-fill: white;-fx-font-family: '" + FONT_FAMILY + "';");
        VBox profileContent = new VBox(10, userName, userId, location);

        ImageView profilePhoto = new ImageView(ProfileDetailData.getImgProfile());
        profilePhoto.setFitWidth(150);
        profilePhoto.setFitHeight(150);
        Circle clipProfilePhoto = new Circle(75, 75, 75);
        profilePhoto.setClip(clipProfilePhoto);

        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setStyle("-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff)");
        editProfileButton.setMinHeight(25);
        editProfileButton.setMaxWidth(100);

        editProfileButton.setOnAction(event -> {
            EditProfile editProfile = new EditProfile();
            EditProfileData editProfileData = new EditProfileData();
            try {
                editProfileData.getEditDetails(ProfileDetailData.getUID());
                VBox editProfileView = editProfile.createEditProfileScene(primaryStage, bp);
                this.bp.setCenter(editProfileView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox editVBox = new VBox(15, editProfileButton);
        HBox profilePic = new HBox(40, profilePhoto, profileContent, editVBox);
        profilePic.setAlignment(Pos.CENTER);

        Separator horizontalSeparator1 = new Separator();
        horizontalSeparator1.setMaxWidth(800);

        String activeTabStyle = "-fx-font-size: 18; -fx-fill: #00c6ff; -fx-font-family: '" + FONT_FAMILY + "';-fx-cursor: hand;";
        String inactiveTabStyle = "-fx-font-size: 18; -fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';-fx-cursor: hand;";

        Text postsTab = new Text("Posts");
        postsTab.setStyle(activeTabStyle);

        Separator verticalSeparator = new Separator(Orientation.VERTICAL);
        verticalSeparator.setMaxHeight(40);

        Text bookmarksTab = new Text("Bookmarks");
        bookmarksTab.setStyle(inactiveTabStyle);

        HBox postDetails = new HBox(20, postsTab, verticalSeparator, bookmarksTab);
        postDetails.setAlignment(Pos.CENTER);

        Separator horizontalSeparator2 = new Separator();
        horizontalSeparator2.setMaxWidth(800);

        postContentArea = new VBox();
        postContentArea.setAlignment(Pos.CENTER);
        postContentArea.setPadding(new Insets(15, 0, 0, 0));

        postsTab.setOnMouseClicked((MouseEvent event) -> {
            postsTab.setStyle(activeTabStyle);
            bookmarksTab.setStyle(inactiveTabStyle);
            loadUserPosts();
        });

        bookmarksTab.setOnMouseClicked((MouseEvent event) -> {
            bookmarksTab.setStyle(activeTabStyle);
            postsTab.setStyle(inactiveTabStyle);
            loadSavedPosts();
        });

        loadUserPosts();

        VBox mainVb = new VBox(15, profilePic, horizontalSeparator1, postDetails, horizontalSeparator2, postContentArea);
        ScrollPane scrollPage = new ScrollPane(mainVb);
        scrollPage.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPage.setFitToWidth(true);

        scrollPage.setStyle("-fx-background: black; -fx-background-color: black;");
        mainVb.setStyle("-fx-background-color: black;");

        VBox mainContainer = new VBox(scrollPage);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: black;");

        return mainContainer;
    }

    private void loadUserPosts() {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        postContentArea.getChildren().setAll(loadingIndicator);

        Task<ObservableList<Post>> fetchPostsTask = new Task<>() {
            @Override
            protected ObservableList<Post> call() throws Exception {

                ProfileDetailData.refreshUserDetails();

                List<String> postIds = ProfileDetailData.getUserPosts();
                String currentUserId = ProfileDetailData.getUID();
                return UserPostDao.fetchPostsByIds(postIds, currentUserId);
            }
        };

        fetchPostsTask.setOnSucceeded(event -> {
            ObservableList<Post> userPosts = fetchPostsTask.getValue();
            if (userPosts.isEmpty()) {
                Text noPostsText = new Text("No posts yet.");
                noPostsText.setStyle("-fx-fill: white; -fx-font-size: 16; -fx-font-family: '" + FONT_FAMILY + "';");
                postContentArea.getChildren().setAll(noPostsText);
            } else {
                ListView<Post> postsListView = new ListView<>(userPosts);
                postsListView.setCellFactory(lv -> new HomeMain().new PostListCell());
                postsListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");
                postsListView.setMaxWidth(800);
                postsListView.setPrefHeight(userPosts.size() * 450);
                postContentArea.getChildren().setAll(postsListView);
            }
        });

        fetchPostsTask.setOnFailed(event -> {
            fetchPostsTask.getException().printStackTrace();
            Text errorText = new Text("Failed to load posts.");
            errorText.setStyle("-fx-fill: red; -fx-font-size: 16; -fx-font-family: '" + FONT_FAMILY + "';");
            postContentArea.getChildren().setAll(errorText);
        });

        new Thread(fetchPostsTask).start();
    }

    private void loadSavedPosts() {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        postContentArea.getChildren().setAll(loadingIndicator);

        Task<ObservableList<Post>> fetchSavedPostsTask = new Task<>() {
            @Override
            protected ObservableList<Post> call() throws Exception {
                
                ProfileDetailData.refreshUserDetails();

                List<String> savedPostIds = ProfileDetailData.getSavedPosts();
                String currentUserId = ProfileDetailData.getUID();
                return UserPostDao.fetchPostsByIds(savedPostIds, currentUserId);
            }
        };

        fetchSavedPostsTask.setOnSucceeded(event -> {
            ObservableList<Post> savedPosts = fetchSavedPostsTask.getValue();
            if (savedPosts.isEmpty()) {
                Text noPostsText = new Text("No saved posts.");
                noPostsText.setStyle("-fx-fill: white; -fx-font-size: 16; -fx-font-family: '" + FONT_FAMILY + "';");
                postContentArea.getChildren().setAll(noPostsText);
            } else {
                ListView<Post> postsListView = new ListView<>(savedPosts);
                postsListView.setCellFactory(lv -> new HomeMain().new PostListCell());
                postsListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");
                postsListView.setMaxWidth(800);
                postsListView.setPrefHeight(savedPosts.size() * 450);
                postContentArea.getChildren().setAll(postsListView);
            }
        });

        fetchSavedPostsTask.setOnFailed(event -> {
            fetchSavedPostsTask.getException().printStackTrace();
            Text errorText = new Text("Failed to load saved posts.");
            errorText.setStyle("-fx-fill: red; -fx-font-size: 16; -fx-font-family: '" + FONT_FAMILY + "';");
            postContentArea.getChildren().setAll(errorText);
        });

        new Thread(fetchSavedPostsTask).start();
    }
}