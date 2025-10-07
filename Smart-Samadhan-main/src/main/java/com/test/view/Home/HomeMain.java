package com.test.view.Home;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.test.Controller.PerplexityNewsController;
import com.test.Controller.TranslationService;
import com.test.dao.CommentDAO;
import com.test.dao.NotificationDAO;
import com.test.dao.PostDAO;
import com.test.dao.ProfileDetailData;
import com.test.model.Comment;
import com.test.model.Notification;
import com.test.model.Post;
import com.test.servies.InitializeFirbase;
import com.test.util.LanguageManager;
import com.test.view.ChatBot.ChatView;
import com.test.view.CityComplaint.CityComplaintListView;
import com.test.view.LoginPage.Login;
import com.test.view.Post.PostButton;
import com.test.view.Search.Search;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class HomeMain {

    private Stage primaryStage;
    private Scene homeScene;
    private Scene loginScene;
    private BorderPane borderPane;
    private final TranslationService translator = new TranslationService();
    private final LanguageManager lang = LanguageManager.getInstance();

    private String currentIdToken;
    private String currentEmail;

    private static final String FONT_FAMILY = "Poppins";
    public static final Set<String> sessionLikedPostIds = new HashSet<>();
    public static final Set<String> sessionUnlikedPostIds = new HashSet<>();

    static {
        try {
            Font.loadFont(HomeMain.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(HomeMain.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setScene(Scene scene) {
        this.loginScene = scene;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setAuthInfo(String idToken, String email) {
        this.currentIdToken = idToken;
        this.currentEmail = email;
    }

    public Scene getHomeScene(Stage stage) {
        this.primaryStage = stage;

        Parent rootContent = buildHomeUI();

        Label welcomeLabel = new Label(lang.getString("header.welcome") + " " + ProfileDetailData.getFirstName());
        welcomeLabel.setStyle("-fx-text-fill:linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);-fx-font-size:30;-fx-font-weight:bold;-fx-font-family: '" + FONT_FAMILY + "';");
        welcomeLabel.setPadding(new Insets(0, 0, 0, 40));


        Button rankButton = new Button();
        Image rankingImage = new Image("assets\\Images\\ranking.png");
        ImageView rankView = new ImageView(rankingImage);
        rankView.setFitHeight(30);
        rankView.setFitWidth(30);
        rankButton.setGraphic(rankView);
        rankButton.setMinHeight(40);
        rankButton.setMinWidth(40);
        rankButton.setStyle("-fx-background-radius: 17; -fx-border-radius: 17; -fx-background-color: transparent;");


        rankButton.setOnAction(e -> {
            CityComplaintListView rankingView = new CityComplaintListView(borderPane);
            borderPane.setCenter(rankingView.getView());
        });

        ComboBox<String> languageDropdown = new ComboBox<>();
        languageDropdown.getItems().addAll(
                "English", "Hindi", "Marathi", "Gujarati", "Telugu", "Russian", "Chinese", "Spanish", "French"
        );
        languageDropdown.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 20; -fx-mark-color: white; -fx-font-family: '" + FONT_FAMILY + "';");

        String currentLangCode = lang.getCurrentLanguageCode();
        String currentLangName = switch (currentLangCode) {
            case "hi" -> "Hindi";
            case "mr" -> "Marathi";
            case "gu" -> "Gujarati";
            case "te" -> "Telugu";
            case "ru" -> "Russian";
            case "zh" -> "Chinese";
            case "es" -> "Spanish";
            case "fr" -> "French";
            default -> "English";
        };
        languageDropdown.setValue(currentLangName);
        
        languageDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 13));
                    setAlignment(Pos.CENTER);
                    setStyle("-fx-background-color: transparent; -fx-padding: 8 15 8 15;");
                }
            }
        });


        languageDropdown.setOnAction(e -> {
            String selectedLanguage = languageDropdown.getValue();
            String newLangCode = switch (selectedLanguage) {
                case "Hindi" -> "hi";
                case "Marathi" -> "mr";
                case "Gujarati" -> "gu";
                case "Telugu" -> "te";
                case "Russian" -> "ru";
                case "Chinese" -> "zh";
                case "Spanish" -> "es";
                case "French" -> "fr";
                default -> "en";
            };

            if (!lang.getCurrentLanguageCode().equals(newLangCode)) {
                lang.setLanguage(newLangCode);
                primaryStage.setScene(getHomeScene(primaryStage));
            }
        });

        Button notificationButton = new Button();
        Image notificationImage = new Image("assets\\Images\\bell.png");
        ImageView notificationView = new ImageView(notificationImage);
        notificationView.setFitHeight(22);
        notificationView.setFitWidth(22);
        notificationButton.setGraphic(notificationView);
        notificationButton.setMinHeight(40);
        notificationButton.setMinWidth(40);
        notificationButton.setStyle("-fx-background-radius: 17; -fx-border-radius: 17; -fx-background-color: transparent;");

        notificationButton.setOnAction(e -> {
            borderPane.setCenter(createNotificationsView());
            hideScrollBars();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button openChat = new Button(lang.getString("header.assistant"));
        openChat.setOnAction(e -> {
            ChatView chatView = new ChatView();
            borderPane.setCenter(chatView.getView());
        });
        openChat.setStyle("-fx-font-weight: bold;-fx-text-fill:linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-font-size: 25;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");

        Image robo = new Image("assets/Images/robot_630426.png");
        ImageView imageView = new ImageView(robo);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        HBox hb = new HBox(welcomeLabel, spacer,languageDropdown,rankButton,notificationButton, openChat, imageView);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setStyle("-fx-background-color:black");
        hb.setMinHeight(60);
        hb.setPadding(new Insets(20));

        VBox mainLayout = new VBox(hb, rootContent);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(rootContent, Priority.ALWAYS);

        homeScene = new Scene(mainLayout, 1550, 800);
        hideScrollBars();
        return homeScene;
    }

    private Parent buildHomeUI() {
        borderPane = new BorderPane();
        borderPane.setLeft(buildSidebar());
        borderPane.setCenter(buildHomeContent());
        borderPane.setPadding(new Insets(20));
        borderPane.setStyle("-fx-background-color: black;");
        return borderPane;
    }

    private VBox buildSidebar() {
        ImageView logo = new ImageView(new Image("assets\\Images\\Untitled_design__7_-removebg-preview.png"));
        logo.setFitWidth(200);
        logo.setPreserveRatio(true);
        logo.setTranslateX(50);

        VBox logovb = new VBox(logo);
        logovb.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);-fx-background-radius: 25;");
        logovb.setMaxWidth(300);
        logovb.setMinHeight(220);

        Button homeLabel = new Button(lang.getString("sidebar.home"));
        homeLabel.setOnAction(e -> {
            borderPane.setCenter(buildHomeContent());
            hideScrollBars();
        });
        homeLabel.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        homeLabel.setPadding(new Insets(10, 0, 0, 50));
        homeLabel.setOnMouseEntered(e -> homeLabel.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        homeLabel.setOnMouseExited(e -> homeLabel.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));

        Button searchLabel = new Button(lang.getString("sidebar.search"));
        searchLabel.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        searchLabel.setPadding(new Insets(10, 0, 0, 50));
        searchLabel.setOnMouseEntered(e -> searchLabel.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        searchLabel.setOnMouseExited(e -> searchLabel.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        searchLabel.setOnMouseClicked(e -> {
            Search searchPage = new Search();
            Parent searchUI = searchPage.createSearchUI(primaryStage, borderPane);
            borderPane.setCenter(searchUI);
        });

        Button createpost = new Button(lang.getString("sidebar.createPost"));
        createpost.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        createpost.setPadding(new Insets(10, 0, 0, 50));
        createpost.setOnMouseEntered(e -> createpost.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        createpost.setOnMouseExited(e -> createpost.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        createpost.setOnMouseClicked(e -> {
            PostButton postButtonPage = new PostButton();
            Scene uploadScene = postButtonPage.getUploadScene(primaryStage);
            primaryStage.setScene(uploadScene);
        });

        Button profile = new Button(lang.getString("sidebar.profile"));
        profile.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        profile.setPadding(new Insets(10, 0, 0, 50));
        profile.setOnMouseEntered(e -> profile.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        profile.setOnMouseExited(e -> profile.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        profile.setOnMouseClicked(e -> {
            com.test.view.ProfilePage.ProfilePage profilePage = new com.test.view.ProfilePage.ProfilePage();
            Parent profileUI = profilePage.createProfileUI(primaryStage, borderPane);
            borderPane.setCenter(profileUI);
        });

        Button settings = new Button(lang.getString("sidebar.settings"));
        settings.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        settings.setPadding(new Insets(10, 0, 0, 50));
        settings.setOnMouseEntered(e -> settings.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        settings.setOnMouseExited(e -> settings.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        settings.setOnMouseClicked(e -> {
            Settings settingPage = new Settings();
            settingPage.setAuthInfo(currentIdToken, currentEmail);
            borderPane.setCenter(settingPage.createSettingsScene(() -> {
                borderPane.setCenter(buildHomeContent());
                hideScrollBars();
            }, borderPane, primaryStage));
        });

        Button about = new Button(lang.getString("sidebar.about"));
        about.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        about.setPadding(new Insets(10, 0, 0, 50));
        about.setOnMouseEntered(e -> about.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 24;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        about.setOnMouseExited(e -> about.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 20;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        about.setOnMouseClicked(e -> {
            About aboutPage = new About();
            borderPane.setCenter(aboutPage.createAboutScene(() -> {
                borderPane.setCenter(buildHomeContent());
                hideScrollBars();
            }));
        });

        VBox leftBox = new VBox(20, homeLabel, searchLabel, createpost, profile, settings, about);
        leftBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);-fx-background-radius: 25;");
        leftBox.setPrefHeight(550);
        leftBox.setMinWidth(300);
        leftBox.setPadding(new Insets(10, 0, 0, 20));

        VBox sidebar = new VBox(30);
        sidebar.getChildren().addAll(logovb, leftBox);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: transparent;");

        return sidebar;
    }

    private HBox buildHomeContent() {
        VBox feedVBox = createPostFeedVBox();

        Text newsTitle = new Text(lang.getString("news.title"));
        newsTitle.setStyle("-fx-font-weight: bold; -fx-fill:linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff) ; -fx-font-size: 24; -fx-font-family: '" + FONT_FAMILY + "';");

        TextField cityInput = new TextField("Pune");
        cityInput.setPromptText(lang.getString("news.cityPrompt"));
        cityInput.setStyle("-fx-background-radius: 10; -fx-padding: 5; -fx-font-family: '" + FONT_FAMILY + "';");

        Button refreshButton = new Button(lang.getString("news.refresh"));
        refreshButton.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 12;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent");
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-text-fill: #007bff;-fx-font-weight: bold;-fx-font-size: 14;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-font-weight: bold;-fx-text-fill:white; -fx-font-size: 12;-fx-font-family: '" + FONT_FAMILY + "';-fx-background-color:transparent;-fx-border-color:transparent"));

        HBox newsControls = new HBox(10, cityInput, refreshButton);
        newsControls.setAlignment(Pos.CENTER);

        VBox newsCardBox = new VBox(10);
        newsCardBox.setPadding(new Insets(10));

        ScrollPane newsScrollPane = new ScrollPane(newsCardBox);
        newsScrollPane.setFitToWidth(true);
        newsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        newsScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        newsScrollPane.setId("news-scroll-pane");

        ProgressIndicator newsLoader = new ProgressIndicator();
        newsLoader.setVisible(false);
        newsLoader.setMaxSize(50, 50);

        StackPane newsContentPane = new StackPane(newsScrollPane, newsLoader);
        VBox.setVgrow(newsContentPane, Priority.ALWAYS);

        refreshButton.setOnAction(event -> {
            if (!cityInput.getText().trim().isEmpty()) {
                loadNewsInto(cityInput.getText(), newsCardBox, newsLoader, newsScrollPane);
            }
        });

        loadNewsInto(cityInput.getText(), newsCardBox, newsLoader, newsScrollPane);

        VBox newsVBox = new VBox(15, newsTitle, newsControls, newsContentPane);
        newsVBox.setPadding(new Insets(20, 0, 0, 20));
        newsVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");
        newsVBox.setMinWidth(350);
        newsVBox.setAlignment(Pos.TOP_CENTER);

        HBox centerContent = new HBox(20, feedVBox, newsVBox);
        centerContent.setAlignment(Pos.CENTER);
        return centerContent;
    }

    public VBox createPostFeedVBox() {
        ListView<Post> listView = new ListView<>();
        listView.setPrefWidth(700);
        listView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e;");
        listView.setId("post-feed-list");
        listView.setCellFactory(lv -> new PostListCell());
        VBox.setVgrow(listView, Priority.ALWAYS);

        Platform.runLater(() -> listView.lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0)));

        Pane shimmerPane = createFeedShimmerPane();

        StackPane stackPane = new StackPane(listView, shimmerPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        VBox container = new VBox(stackPane);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: #2e2e2e; -fx-background-radius: 25;");
        container.setAlignment(Pos.CENTER);
        VBox.setVgrow(container, Priority.ALWAYS);

        Task<ObservableList<Post>> fetchPostsTask = new Task<>() {
            @Override
            protected ObservableList<Post> call() throws Exception {
                String currentUserId = ProfileDetailData.getUID();
                List<String> savedPostIds = ProfileDetailData.getSavedPosts();
                ObservableList<Post> posts = FXCollections.observableArrayList();
                ApiFuture<QuerySnapshot> future = InitializeFirbase.getdb()
                        .collection("posts")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .get();
                QuerySnapshot querySnapshot = future.get();

                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Timestamp timestamp = document.getTimestamp("timestamp");
                    String formattedDate = "No date";
                    if (timestamp != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        formattedDate = sdf.format(timestamp.toDate());
                    }

                    String postId = document.getId();
                    long likeCount = document.contains("likeCount") ? document.getLong("likeCount") : 0;

                    Post post = new Post(
                            postId,
                            document.getString("username"),
                            document.getString("description"),
                            document.getString("imageUrl"),
                            document.getString("location"),
                            formattedDate,
                            document.getString("profileImage"),
                            (int) likeCount
                    );

                    if (currentUserId != null && document.contains("likes") && document.get("likes") instanceof Map) {
                        Map<String, Object> likesMap = (Map<String, Object>) document.get("likes");
                        if (likesMap.containsKey(currentUserId)) {
                            post.setLikedByCurrentUser(true);
                        }
                    }

                    if (savedPostIds != null && savedPostIds.contains(postId)) {
                        post.setSavedByCurrentUser(true);
                    }

                    posts.add(post);
                }
                return posts;
            }
        };

        fetchPostsTask.setOnSucceeded(e -> {
            shimmerPane.setVisible(false);
            listView.setItems(fetchPostsTask.getValue());
        });

        fetchPostsTask.setOnFailed(e -> {
            shimmerPane.setVisible(false);
            Label errorLabel = new Label(lang.getString("post.loadError"));
            errorLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 10; -fx-background-radius: 10; -fx-font-family: '" + FONT_FAMILY + "';");
            stackPane.getChildren().add(errorLabel);
            fetchPostsTask.getException().printStackTrace();
        });

        new Thread(fetchPostsTask).start();

        return container;
    }

    private Pane createFeedShimmerPane() {
        VBox skeletonContainer = new VBox(25);
        skeletonContainer.setPadding(new Insets(15));
        for (int i = 0; i < 3; i++) {
            skeletonContainer.getChildren().add(createShimmerPlaceholderCard());
        }

        Rectangle shimmerHighlight = new Rectangle();
        LinearGradient shimmerGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT), new Stop(0.4, Color.TRANSPARENT),
                new Stop(0.5, Color.web("white", 0.3)), new Stop(0.6, Color.TRANSPARENT),
                new Stop(1, Color.TRANSPARENT));
        shimmerHighlight.setFill(shimmerGradient);
        shimmerHighlight.widthProperty().bind(skeletonContainer.widthProperty());
        shimmerHighlight.heightProperty().bind(skeletonContainer.heightProperty());
        shimmerHighlight.setClip(skeletonContainer);

        TranslateTransition shimmerAnimation = new TranslateTransition(Duration.seconds(2.0), shimmerHighlight);
        shimmerAnimation.setFromX(-700);
        shimmerAnimation.setToX(700);
        shimmerAnimation.setCycleCount(Animation.INDEFINITE);
        shimmerAnimation.setInterpolator(Interpolator.EASE_BOTH);
        shimmerAnimation.play();

        return new StackPane(shimmerHighlight);
    }

    private VBox createShimmerPlaceholderCard() {
        Circle avatar = new Circle(20, Color.web("#404040"));
        VBox textLines = new VBox(5,
                new Rectangle(120, 12, Color.web("#404040")),
                new Rectangle(80, 10, Color.web("#404040"))
        );
        HBox header = new HBox(10, avatar, textLines);
        header.setAlignment(Pos.CENTER_LEFT);

        Rectangle imagePlaceholder = new Rectangle(630, 250, Color.web("#404040"));
        imagePlaceholder.setArcWidth(20);
        imagePlaceholder.setArcHeight(20);

        VBox descriptionLines = new VBox(5,
                new Rectangle(600, 10, Color.web("#404040")),
                new Rectangle(550, 10, Color.web("#404040")),
                new Rectangle(400, 10, Color.web("#404040"))
        );

        VBox card = new VBox(15, header, imagePlaceholder, descriptionLines);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 15;");
        return card;
    }

    private void loadNewsInto(String city, VBox newsCardBox, ProgressIndicator loader, ScrollPane newsScrollPane) {
        loader.setVisible(true);
        newsScrollPane.setOpacity(0.5);

        Task<String> newsTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                PerplexityNewsController perplexityController = new PerplexityNewsController();
                return perplexityController.getCurrentCivicNews(city);
            }
        };

        newsTask.setOnSucceeded(e -> {
            String civicNews = newsTask.getValue();
            populateNewsContent(newsCardBox, civicNews, newsScrollPane);
            loader.setVisible(false);
            newsScrollPane.setOpacity(1.0);
        });

        newsTask.setOnFailed(e -> {
            Throwable exception = newsTask.getException();
            String errorMessage = lang.getString("news.error");
            if (exception != null) {
                errorMessage += "\n" + exception.getMessage();
            }
            populateNewsContent(newsCardBox, errorMessage, newsScrollPane);
            loader.setVisible(false);
            newsScrollPane.setOpacity(1.0);
        });

        new Thread(newsTask).start();
    }

    private void populateNewsContent(VBox newsCardBox, String civicNews, ScrollPane newsScrollPane) {
        newsCardBox.getChildren().clear();
        if (civicNews == null || civicNews.trim().isEmpty() || civicNews.startsWith("Error")) {
            String message = (civicNews == null || civicNews.trim().isEmpty()) ? lang.getString("news.noNews") : civicNews;
            Text errorText = new Text(message);
            errorText.setFill(Color.WHITE);
            errorText.setFont(Font.font(FONT_FAMILY, 14));
            errorText.wrappingWidthProperty().bind(newsScrollPane.widthProperty().subtract(35));
            newsCardBox.getChildren().add(errorText);
            return;
        }

        String[] newsItems = civicNews.split("\\n\\s*\\n|\\n(?=\\d+\\.)");
        for (String item : newsItems) {
            String cleanItem = item.replaceAll("^\\d+\\.\\s*", "").trim();
            if (cleanItem.isEmpty()) continue;

            Text titleText = new Text(cleanItem);
            titleText.setFill(Color.WHITE);
            titleText.setFont(Font.font(FONT_FAMILY, 16));
            titleText.setTextAlignment(TextAlignment.JUSTIFY);
            titleText.wrappingWidthProperty().bind(newsScrollPane.widthProperty().subtract(45));

            ProgressIndicator translationLoader = new ProgressIndicator();
            translationLoader.setMaxSize(30, 30);
            translationLoader.setVisible(false);

            StackPane textContainer = new StackPane(titleText, translationLoader);
            textContainer.setAlignment(Pos.CENTER);

            ComboBox<String> languageCombo = new ComboBox<>();
            languageCombo.getItems().addAll("English", "Marathi", "Hindi", "Gujarati", "Telugu", "Russian", "Chinese", "Spanish", "French");
            languageCombo.setPromptText(lang.getString("news.translate"));
            languageCombo.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 20; -fx-mark-color: white; -fx-font-family: '" + FONT_FAMILY + "';");

            languageCombo.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? lang.getString("news.translate") : item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-size: 12px; -fx-padding: 3 5 3 5; -fx-font-family: '" + FONT_FAMILY + "';");
                }
            });

            VBox newsCard = new VBox(10, textContainer, languageCombo);
            newsCard.setPadding(new Insets(10));
            newsCard.setStyle("-fx-background-color: #333; -fx-background-radius: 10;");
            newsCard.setUserData(cleanItem);

            languageCombo.setOnAction(event -> {
                String selectedLanguage = languageCombo.getValue();
                String originalText = (String) newsCard.getUserData();

                if (selectedLanguage != null && !selectedLanguage.isEmpty() && originalText != null) {
                    Task<String> translationTask = new Task<>() {
                        @Override
                        protected String call() throws Exception {
                            return translator.translateText(originalText, selectedLanguage);
                        }
                    };
                    translationLoader.setVisible(true);
                    titleText.setOpacity(0.4);
                    translationTask.setOnSucceeded(e -> {
                        titleText.setText(translationTask.getValue());
                        translationLoader.setVisible(false);
                        titleText.setOpacity(1.0);
                    });
                    translationTask.setOnFailed(e -> {
                        titleText.setText(lang.getString("post.translationFailed"));
                        translationLoader.setVisible(false);
                        titleText.setOpacity(1.0);
                    });
                    new Thread(translationTask).start();
                }
            });
            newsCardBox.getChildren().add(newsCard);
        }
    }

    private VBox createNotificationsView() {
        VBox notificationsLayout = new VBox(20);
        notificationsLayout.setPadding(new Insets(25));
        notificationsLayout.setAlignment(Pos.TOP_CENTER);
        notificationsLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        Label title = new Label(lang.getString("notifications.title"));
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-font-family: '" + FONT_FAMILY + "';");

        ListView<Notification> notificationListView = new ListView<>();
        notificationListView.setStyle("-fx-background-color: #2e2e2e; -fx-control-inner-background: #2e2e2e; -fx-background-radius: 15;");
        VBox.setVgrow(notificationListView, Priority.ALWAYS);

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(true);

        StackPane listContainer = new StackPane(loadingIndicator);
        VBox.setVgrow(listContainer, Priority.ALWAYS);

        Task<ObservableList<Notification>> fetchNotificationsTask = new Task<>() {
            @Override
            protected ObservableList<Notification> call() throws Exception {
                NotificationDAO notificationDAO = new NotificationDAO();
                String currentUserId = ProfileDetailData.getUID();
                if (currentUserId == null || currentUserId.isEmpty()) {
                    return FXCollections.observableArrayList();
                }

                ApiFuture<QuerySnapshot> future = notificationDAO.getNotificationsForUser(currentUserId);
                QuerySnapshot querySnapshot = future.get();

                List<Notification> notifications = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Notification notification = document.toObject(Notification.class);
                    notification.setNotificationId(document.getId());
                    notifications.add(notification);
                }
                return FXCollections.observableArrayList(notifications);
            }
        };

        fetchNotificationsTask.setOnSucceeded(e -> {
            listContainer.getChildren().clear();
            listContainer.getChildren().add(notificationListView);
            ObservableList<Notification> notifications = fetchNotificationsTask.getValue();
            notificationListView.setItems(notifications);

            if (notifications.isEmpty()) {
                Label noNotificationsLabel = new Label(lang.getString("notifications.empty"));
                noNotificationsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #888888; -fx-font-family: '" + FONT_FAMILY + "';");
                listContainer.getChildren().add(noNotificationsLabel);
                StackPane.setAlignment(noNotificationsLabel, Pos.CENTER);
            }
        });

        fetchNotificationsTask.setOnFailed(e -> {
            listContainer.getChildren().clear();
            Label errorLabel = new Label(lang.getString("notifications.error"));
            errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff8888; -fx-font-family: '" + FONT_FAMILY + "';");
            listContainer.getChildren().add(errorLabel);
            fetchNotificationsTask.getException().printStackTrace();
        });

        new Thread(fetchNotificationsTask).start();

        notificationListView.setCellFactory(lv -> new ListCell<>() {
            private final VBox content = new VBox(5);
            private final Label messageLabel = new Label();
            private final Label timeLabel = new Label();

            {
                messageLabel.setWrapText(true);
                timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaaaaa; -fx-font-family: '" + FONT_FAMILY + "';");
                content.getChildren().addAll(messageLabel, timeLabel);
                setGraphic(content);
                setStyle("-fx-padding: 15; -fx-background-color: #3a3a3a; -fx-border-color: #4f4f4f; -fx-border-width: 0 0 1 0;");
            }

            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: #2e2e2e;");
                } else {
                    messageLabel.setText("🔔  " + item.getMessage());

                    if (item.getTimestamp() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a");
                        timeLabel.setText(sdf.format(item.getTimestamp().toDate()));
                    } else {
                        timeLabel.setText("");
                    }

                    if (item.isRead()) {
                        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #aaaaaa; -fx-font-weight: normal; -fx-font-family: '" + FONT_FAMILY + "';");
                    } else {
                        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");
                    }
                    setGraphic(content);
                }
            }
        });

        notificationsLayout.getChildren().addAll(title, listContainer);
        return notificationsLayout;
    }

    private void hideScrollBars() {
        Platform.runLater(() -> {
            if (homeScene == null) return;

            Node postFeedNode = homeScene.lookup("#post-feed-list");
            if (postFeedNode != null) {
                Set<Node> scrollBars = postFeedNode.lookupAll(".scroll-bar");
                for (Node bar : scrollBars) {
                    bar.setOpacity(0);
                }
            }

            Node newsPaneNode = homeScene.lookup("#news-scroll-pane");
            if (newsPaneNode != null) {
                Set<Node> scrollBars = newsPaneNode.lookupAll(".scroll-bar");
                for (Node bar : scrollBars) {
                    bar.setOpacity(0);
                }
            }
        });
    }

    private void logoutUser() {
        currentIdToken = null;
        currentEmail = null;
        ProfileDetailData.clearUserDetails();

        sessionLikedPostIds.clear();
        sessionUnlikedPostIds.clear();

        if (loginScene != null) {
            primaryStage.setScene(loginScene);
            System.out.println("Logged out. Navigating back to login.");
        } else {
            System.out.println("Logout successful, but login scene reference is null. Re-creating login scene.");
            Login loginView = new Login();
            loginView.setStage(primaryStage);
            primaryStage.setScene(loginView.createScene(primaryStage));
        }
    }

    public class PostListCell extends ListCell<Post> {
        private final VBox cellLayout = new VBox(15);
        private final HBox postHeader = new HBox(10);
        private final ImageView userAvatarView = new ImageView();
        private final ImageView postImageView = new ImageView();
        private final Label usernameLabel = new Label();
        private final Label locationLabel = new Label();
        private final Label timestampLabel = new Label();
        private final Label descriptionLabel = new Label();
        private final Image userPlaceholder = new Image("assets/Images/WhatsApp Image 2025-07-18 at 18.05.23_f29502e4.jpg");
        private final StackPane imageContainer = new StackPane();
        private final Rectangle shimmerPlaceholder = new Rectangle();
        private final Rectangle shimmerHighlight = new Rectangle();
        private final TranslateTransition shimmerAnimation;

        private final Button likeButton = new Button();
        private final Button commentButton = new Button();
        private final Button saveButton = new Button();
        private final Label likeCountLabel = new Label();
        private final ComboBox<String> translationDropdown = new ComboBox<>();
        private final HBox actionButtonsBox = new HBox(15);
        private boolean isLiked;
        private boolean isSaved;
        private final PostDAO postDAO = new PostDAO();
        private final CommentDAO commentDAO = new CommentDAO();
        private String originalDescription;
        private final StackPane descriptionContainer = new StackPane();
        private final ProgressIndicator descriptionLoader = new ProgressIndicator();

        public PostListCell() {
            userAvatarView.setFitHeight(40);
            userAvatarView.setFitWidth(40);
            userAvatarView.setClip(new Circle(20, 20, 20));

            usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
            locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #cccccc; -fx-font-family: '" + FONT_FAMILY + "';");
            VBox userInfoBox = new VBox(2, usernameLabel, locationLabel);

            timestampLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaaaaa; -fx-alignment: top-right; -fx-font-family: '" + FONT_FAMILY + "';");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            postHeader.getChildren().addAll(userAvatarView, userInfoBox, spacer, timestampLabel);
            postHeader.setAlignment(Pos.CENTER_LEFT);

            shimmerPlaceholder.setFill(Color.web("#404040"));
            shimmerPlaceholder.widthProperty().bind(imageContainer.widthProperty());
            shimmerPlaceholder.heightProperty().bind(imageContainer.heightProperty());
            shimmerPlaceholder.setArcWidth(20);
            shimmerPlaceholder.setArcHeight(20);

            LinearGradient shimmerGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#404040")), new Stop(0.4, Color.web("#404040")),
                    new Stop(0.5, Color.web("#555555")), new Stop(0.6, Color.web("#404040")),
                    new Stop(1, Color.web("#404040")));
            shimmerHighlight.setFill(shimmerGradient);
            shimmerHighlight.widthProperty().bind(imageContainer.widthProperty());
            shimmerHighlight.heightProperty().bind(imageContainer.heightProperty());
            shimmerHighlight.setArcWidth(20);
            shimmerHighlight.setArcHeight(20);

            shimmerAnimation = new TranslateTransition(Duration.seconds(1.5), shimmerHighlight);
            shimmerAnimation.setFromX(-700);
            shimmerAnimation.setToX(700);
            shimmerAnimation.setCycleCount(Animation.INDEFINITE);
            shimmerAnimation.setInterpolator(Interpolator.EASE_BOTH);

            imageContainer.setStyle("-fx-background-color: transparent;");
            imageContainer.getChildren().addAll(shimmerPlaceholder, shimmerHighlight, postImageView);

            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(imageContainer.widthProperty());
            clip.heightProperty().bind(imageContainer.heightProperty());
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageContainer.setClip(clip);

            postImageView.setPreserveRatio(true);
            postImageView.setSmooth(true);
            postImageView.fitWidthProperty().bind(imageContainer.widthProperty());

            descriptionLabel.setWrapText(true);
            descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #dddddd; -fx-font-family: '" + FONT_FAMILY + "';");

            descriptionLoader.setMaxSize(30, 30);
            descriptionLoader.setVisible(false);
            descriptionContainer.getChildren().addAll(descriptionLabel, descriptionLoader);
            descriptionContainer.setAlignment(Pos.CENTER);

            setupActionButtonsBox();

            cellLayout.getChildren().addAll(postHeader, imageContainer, descriptionContainer, actionButtonsBox);
            cellLayout.setPadding(new Insets(15));
            cellLayout.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 15;");

            listViewProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    cellLayout.prefWidthProperty().bind(newVal.widthProperty().subtract(40));
                }
            });

            setPadding(new Insets(0, 0, 15, 0));
            setStyle("-fx-background-color: transparent;");
        }

        private void setupActionButtonsBox() {
            likeCountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "';");
            String buttonStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent; -fx-cursor: hand; -fx-font-family: '" + FONT_FAMILY + "';";
            commentButton.setStyle(buttonStyle);
            saveButton.setStyle(buttonStyle);

            translationDropdown.getItems().addAll("Original", "English", "Marathi", "Hindi", "Gujarati", "Telugu", "Russian", "Chinese", "Spanish", "French");
            translationDropdown.setPromptText(lang.getString("post.translate"));
            translationDropdown.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 20; -fx-mark-color: white; -fx-font-family: '" + FONT_FAMILY + "';");

            translationDropdown.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? lang.getString("post.translate") : item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-size: 12px; -fx-padding: 3 5 3 5; -fx-cursor: hand; -fx-font-family: '" + FONT_FAMILY + "';");
                }
            });

            Region buttonSpacer = new Region();
            HBox.setHgrow(buttonSpacer, Priority.ALWAYS);
            actionButtonsBox.setAlignment(Pos.CENTER_LEFT);
            actionButtonsBox.setPadding(new Insets(10, 0, 0, 0));
            actionButtonsBox.getChildren().addAll(likeButton, likeCountLabel, commentButton, buttonSpacer, saveButton, translationDropdown);
        }

        @Override
        protected void updateItem(Post post, boolean empty) {
            super.updateItem(post, empty);
            if (empty || post == null) {
                setGraphic(null);
            } else {
                usernameLabel.setText(post.getUsername());
                locationLabel.setText("📍 " + post.getLocation());
                timestampLabel.setText(post.getTimestamp());
                this.originalDescription = post.getDescription();
                descriptionLabel.setText(this.originalDescription);
                descriptionLabel.setOpacity(1.0);
                descriptionLoader.setVisible(false);

                String profileUrl = post.getProfileUrl();
                if (profileUrl != null && !profileUrl.trim().isEmpty()) {
                    Image profileImage = new Image(profileUrl, true);
                    profileImage.errorProperty().addListener((observable, oldValue, wasError) -> {
                        if (wasError) userAvatarView.setImage(userPlaceholder);
                    });
                    userAvatarView.setImage(profileImage);
                } else {
                    userAvatarView.setImage(userPlaceholder);
                }

                commentButton.setText(lang.getString("post.comment"));
                this.isLiked = post.isLikedByCurrentUser();
                updateLikeButtonState();
                likeCountLabel.setText(post.getLikeCount() + " " + lang.getString("post.likes"));
                likeButton.setOnAction(e -> handleLikeAction(post));

                this.isSaved = post.isSavedByCurrentUser();
                updateSaveButtonState();
                saveButton.setOnAction(e -> handleSaveAction(post));

                commentButton.setOnAction(e -> handleCommentAction(post));
                translationDropdown.setValue(null);
                translationDropdown.setOnAction(e -> handleTranslation(post));

                boolean hasImage = post.getPostImageUrl() != null && !post.getPostImageUrl().isEmpty();
                postImageView.setVisible(false);
                postImageView.setImage(null);
                if (hasImage) {
                    imageContainer.setManaged(true);
                    imageContainer.setVisible(true);
                    imageContainer.setPrefHeight(250);
                    shimmerPlaceholder.setVisible(true);
                    shimmerHighlight.setVisible(true);
                    shimmerAnimation.play();

                    Image newImage = new Image(post.getPostImageUrl(), true);
                    newImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                        if (newProgress.doubleValue() >= 1.0) {
                            double aspectRatio = newImage.getWidth() / newImage.getHeight();
                            double finalHeight = imageContainer.getWidth() / aspectRatio;
                            imageContainer.setPrefHeight(finalHeight);
                            shimmerAnimation.stop();
                            shimmerPlaceholder.setVisible(false);
                            shimmerHighlight.setVisible(false);
                            postImageView.setImage(newImage);
                            postImageView.setVisible(true);
                        }
                    });
                } else {
                    imageContainer.setManaged(false);
                    imageContainer.setVisible(false);
                    shimmerAnimation.stop();
                }
                setGraphic(cellLayout);
            }
        }

        private void handleCommentAction(Post post) {
            showCommentWindow(post);
        }

        private void showCommentWindow(Post post) {
            Stage commentStage = new Stage();
            commentStage.initModality(Modality.APPLICATION_MODAL);
            commentStage.initOwner(primaryStage);
            commentStage.initStyle(StageStyle.UTILITY);
            commentStage.setTitle(lang.getString("comment.windowTitle") + " " + post.getUsername() + "'s post");

            VBox root = new VBox(10);
            root.setPadding(new Insets(15));
            root.setStyle("-fx-background-color: #2e2e2e;");

            Label titleLabel = new Label(lang.getString("comment.title"));
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

            ListView<Comment> commentListView = new ListView<>();
            commentListView.setStyle("-fx-background-color: #3a3a3a; -fx-control-inner-background: #3a3a3a;");
            VBox.setVgrow(commentListView, Priority.ALWAYS);
            commentListView.setCellFactory(lv -> new CommentListCell());

            ProgressIndicator loadingIndicator = new ProgressIndicator();
            loadingIndicator.setMaxSize(40, 40);
            StackPane listContainer = new StackPane(commentListView, loadingIndicator);

            TextField commentInputField = new TextField();
            commentInputField.setPromptText(lang.getString("comment.prompt"));
            commentInputField.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-background-radius: 10; -fx-font-family: '" + FONT_FAMILY + "';");
            HBox.setHgrow(commentInputField, Priority.ALWAYS);

            Button postCommentButton = new Button(lang.getString("comment.postButton"));
            postCommentButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-family: '" + FONT_FAMILY + "';");
            postCommentButton.setDisable(true);
            commentInputField.textProperty().addListener((obs, old, text) -> postCommentButton.setDisable(text.trim().isEmpty()));

            HBox inputArea = new HBox(10, commentInputField, postCommentButton);
            inputArea.setAlignment(Pos.CENTER);

            root.getChildren().addAll(titleLabel, listContainer, inputArea);

            Runnable fetchComments = () -> {
                loadingIndicator.setVisible(true);
                commentListView.setItems(null);

                Task<List<Comment>> fetchTask = new Task<>() {
                    @Override
                    protected List<Comment> call() throws Exception {
                        QuerySnapshot snapshot = commentDAO.getCommentsForPost(post.getPostId()).get();
                        List<Comment> comments = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                            comments.add(doc.toObject(Comment.class));
                        }
                        return comments;
                    }
                };

                fetchTask.setOnSucceeded(e -> {
                    commentListView.setItems(FXCollections.observableArrayList(fetchTask.getValue()));
                    loadingIndicator.setVisible(false);
                });

                fetchTask.setOnFailed(e -> {
                    loadingIndicator.setVisible(false);
                    fetchTask.getException().printStackTrace();
                });
                new Thread(fetchTask).start();
            };

            postCommentButton.setOnAction(e -> {
                String commentText = commentInputField.getText().trim();
                if (!commentText.isEmpty()) {
                    String userId = ProfileDetailData.getUID();
                    String username = ProfileDetailData.getFirstName();
                    if (userId == null || username == null) {
                        System.err.println("Error: User is not logged in. Cannot post comment.");
                        return;
                    }
                    Comment newComment = new Comment(username, commentText, userId, Timestamp.now());
                    postCommentButton.setDisable(true);
                    commentInputField.setDisable(true);
                    commentDAO.addComment(post.getPostId(), newComment).addListener(() -> {
                        Platform.runLater(() -> {
                            commentInputField.clear();
                            commentInputField.setDisable(false);
                            fetchComments.run();
                        });
                    }, Platform::runLater);
                }
            });

            fetchComments.run();

            Scene scene = new Scene(root, 450, 500);
            commentStage.setScene(scene);
            commentStage.show();
        }

        private void handleTranslation(Post post) {
            String selectedLanguage = translationDropdown.getValue();
            if (selectedLanguage == null || selectedLanguage.isEmpty() || this.originalDescription == null) {
                return;
            }

            if (selectedLanguage.equals("Original")) {
                descriptionLabel.setText(this.originalDescription);
                descriptionLoader.setVisible(false);
                descriptionLabel.setOpacity(1.0);
                return;
            }

            descriptionLoader.setVisible(true);
            descriptionLabel.setOpacity(0.5);

            Task<String> translationTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return translator.translateText(originalDescription, selectedLanguage);
                }
            };

            translationTask.setOnSucceeded(e -> {
                descriptionLabel.setText(translationTask.getValue());
                descriptionLoader.setVisible(false);
                descriptionLabel.setOpacity(1.0);
            });

            translationTask.setOnFailed(e -> {
                descriptionLabel.setText(originalDescription);
                descriptionLoader.setVisible(false);
                descriptionLabel.setOpacity(1.0);
                System.err.println(lang.getString("post.translationFailed"));
                e.getSource().getException().printStackTrace();
            });

            new Thread(translationTask).start();
        }

        private void handleLikeAction(Post post) {
            String currentUserId = ProfileDetailData.getUID();
            if (currentUserId == null || currentUserId.isEmpty()) {
                System.err.println("Cannot like post: User not logged in.");
                return;
            }

            boolean isNowLiked = !post.isLikedByCurrentUser();
            post.setLikedByCurrentUser(isNowLiked);
            this.isLiked = isNowLiked;

            if (isNowLiked) {
                post.setLikeCount(post.getLikeCount() + 1);
                sessionLikedPostIds.add(post.getPostId());
                sessionUnlikedPostIds.remove(post.getPostId());
                postDAO.likePost(post.getPostId(), currentUserId);
            } else {
                post.setLikeCount(post.getLikeCount() - 1);
                sessionUnlikedPostIds.add(post.getPostId());
                sessionLikedPostIds.remove(post.getPostId());
                postDAO.unlikePost(post.getPostId(), currentUserId);
            }

            updateLikeButtonState();
            likeCountLabel.setText(post.getLikeCount() + " " + lang.getString("post.likes"));
        }

        private void handleSaveAction(Post post) {
            String currentUserId = ProfileDetailData.getUID();
            if (currentUserId == null || currentUserId.isEmpty()) {
                System.err.println("Cannot save post: User not logged in.");
                return;
            }
            isSaved = !isSaved;
            post.setSavedByCurrentUser(isSaved);

            if (isSaved) {
                ProfileDetailData.getSavedPosts().add(post.getPostId());
                new Thread(() -> postDAO.savePost(post.getPostId(), currentUserId)).start();
            } else {
                ProfileDetailData.getSavedPosts().remove(post.getPostId());
                new Thread(() -> postDAO.unsavePost(post.getPostId(), currentUserId)).start();
            }
            updateSaveButtonState();
        }

        private void updateLikeButtonState() {
            String baseStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: transparent; -fx-cursor: hand; -fx-font-family: '" + FONT_FAMILY + "';";
            String normalStyle = baseStyle + "-fx-text-fill: white;";
            String likedStyle = baseStyle + "-fx-text-fill: #007bff;";
            likeButton.setStyle(isLiked ? likedStyle : normalStyle);
            likeButton.setText(isLiked ? lang.getString("post.liked") : lang.getString("post.like"));
        }

        private void updateSaveButtonState() {
            String baseStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: transparent; -fx-cursor: hand; -fx-font-family: '" + FONT_FAMILY + "';";
            String normalStyle = baseStyle + "-fx-text-fill: white;";
            String savedStyle = baseStyle + "-fx-text-fill: #00c6ff;";
            saveButton.setStyle(isSaved ? savedStyle : normalStyle);
            saveButton.setText(isSaved ? lang.getString("post.saved") : lang.getString("post.save"));
        }
    }

    private static class CommentListCell extends ListCell<Comment> {
        private final HBox layout = new HBox(10);
        private final Label usernameLabel = new Label();
        private final Label commentTextLabel = new Label();

        public CommentListCell() {
            usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
            commentTextLabel.setStyle("-fx-text-fill: #dddddd; -fx-font-family: '" + FONT_FAMILY + "';");
            commentTextLabel.setWrapText(true);
            layout.getChildren().addAll(usernameLabel, commentTextLabel);
            layout.setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(5));
        }

        @Override
        protected void updateItem(Comment comment, boolean empty) {
            super.updateItem(comment, empty);
            if (empty || comment == null) {
                setGraphic(null);
            } else {
                usernameLabel.setText(comment.getUsername() + ":");
                commentTextLabel.setText(comment.getText());
                setGraphic(layout);
            }
        }
    }
}