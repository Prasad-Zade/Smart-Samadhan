package com.test.view.Search;

import com.test.dao.SearchDAO;
import com.test.model.UserSearchResult;
import com.test.util.LanguageManager;
import com.test.view.ProfilePage.CommunitiesPage;
import com.test.view.ProfilePage.OtherUserProfilePage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class Search {

    private Stage primaryStage;
    private BorderPane borderPane;
    private VBox root;
    private VBox contentBox;
    private final SearchDAO searchDAO = new SearchDAO();
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Search.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Search.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Parent createSearchUI(Stage stage, BorderPane borderpane) {
        this.primaryStage = stage;
        this.borderPane = borderpane;

        TextField searchbarTextField = new TextField();
        searchbarTextField.setPromptText("Search for a user by username...");
        searchbarTextField.setMinWidth(700);
        searchbarTextField.setPrefHeight(40);
        searchbarTextField.setFocusTraversable(false);
        searchbarTextField.setStyle(
            "-fx-font-style: italic; -fx-background-radius: 17; -fx-border-radius: 17;" +
            "-fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold; -fx-background-color:#2e2e2e;-fx-prompt-text-fill:white;-fx-text-fill: white;"
        );

        Button searchButton = new Button();
        ImageView iView = new ImageView("Assets/Images/search (1).png");
        iView.setFitHeight(22);
        iView.setFitWidth(22);
        searchButton.setGraphic(iView);
        searchButton.setMinHeight(40);
        searchButton.setMinWidth(40);
        searchButton.setStyle("-fx-background-radius: 17; -fx-border-radius: 17; -fx-background-color: #007bff;");

        searchButton.setOnAction(e -> {
            String searchText = searchbarTextField.getText();
            performUserSearch(searchText);
        });

        Text forYou = new Text(lang.getString("search.trending"));
        forYou.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white; -fx-cursor: hand;");
        forYou.setOnMouseClicked((MouseEvent e) -> showTrendingContent());

        Text community = new Text(lang.getString("search.community"));
        community.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white; -fx-cursor: hand;");
        community.setOnMouseClicked((MouseEvent e) -> showCommunitiesContent());


        HBox sortHBox = new HBox(30, forYou,community);
        HBox searchHBox = new HBox(10, searchbarTextField, searchButton);

        contentBox = new VBox();
        contentBox.setPadding(new Insets(20, 0, 0, 0));

        root = new VBox(20, searchHBox, sortHBox, contentBox);
        root.setPadding(new Insets(30, 240, 10, 250));
        root.setStyle("-fx-background-color: black;");

        showTrendingContent();

        return root;
    }

    private void performUserSearch(String searchText) {
        ProgressIndicator loader = new ProgressIndicator();
        contentBox.getChildren().setAll(loader);

        Task<List<UserSearchResult>> searchTask = new Task<>() {
            @Override
            protected List<UserSearchResult> call() throws Exception {
                if (searchText == null || searchText.trim().isEmpty()) {
                    return List.of();
                }
                return searchDAO.searchForUsers(searchText);
            }
        };

        searchTask.setOnSucceeded(e -> {
            List<UserSearchResult> results = searchTask.getValue();
            if (results.isEmpty()) {
                if (searchText == null || searchText.trim().isEmpty()) {
                    showTrendingContent();
                } else {
                    Text noUsersText = new Text("No users found for '" + searchText + "'.");
                    noUsersText.setStyle("-fx-fill: white; -fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "';");
                    contentBox.getChildren().setAll(noUsersText);
                }
            } else {
                ObservableList<UserSearchResult> userList = FXCollections.observableArrayList(results);
                ListView<UserSearchResult> resultsListView = new ListView<>(userList);
                resultsListView.setCellFactory(lv -> new UserSearchListCell());
                resultsListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");
                contentBox.getChildren().setAll(resultsListView);
            }
        });

        new Thread(searchTask).start();
    }

    private void showTrendingContent() {
        Trending trendingPage = new Trending();
        VBox trendingContentBox = trendingPage.getTrendingContentOnly();
        contentBox.getChildren().setAll(trendingContentBox);
    }

    private void showCommunitiesContent() {
        CommunitiesPage communitiesPage = new CommunitiesPage(borderPane);
        VBox communitiesContentBox = communitiesPage.getCommunitiesContent();
        contentBox.getChildren().setAll(communitiesContentBox);
    }

    private class UserSearchListCell extends ListCell<UserSearchResult> {
        private HBox hbox;
        private ImageView avatar;
        private Text nameText;
        private Text usernameText;
        private final Image placeholder = new Image("assets/Images/WhatsApp Image 2025-07-18 at 18.05.23_f29502e4.jpg");

        public UserSearchListCell() {
            super();
            hbox = new HBox(10);
            avatar = new ImageView();
            nameText = new Text();
            usernameText = new Text();

            avatar.setFitHeight(30);
            avatar.setFitWidth(30);
            avatar.setClip(new Circle(15, 15, 15));

            nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
            usernameText.setStyle("-fx-font-size: 12; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: grey;");

            VBox nameVBox = new VBox(3, nameText, usernameText);
            hbox.getChildren().addAll(avatar, nameVBox);
            hbox.setPadding(new Insets(5));
            hbox.setStyle("-fx-cursor: hand;");

            hbox.setOnMouseClicked(event -> {
                if (getItem() != null) {
                    String userId = getItem().getUserId();
                    OtherUserProfilePage profilePage = new OtherUserProfilePage();
                    VBox profileView = profilePage.createProfileUI(userId);
                    borderPane.setCenter(profileView);
                }
            });

            setStyle("-fx-background-color: black;");
        }

        @Override
        protected void updateItem(UserSearchResult user, boolean empty) {
            super.updateItem(user, empty);
            if (empty || user == null) {
                setGraphic(null);
            } else {
                nameText.setText(user.getFullName());
                usernameText.setText("@" + user.getUsername());
                if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                    avatar.setImage(new Image(user.getProfileImageUrl(), true));
                } else {
                    avatar.setImage(placeholder);
                }
                setGraphic(hbox);
            }
        }
    }
}