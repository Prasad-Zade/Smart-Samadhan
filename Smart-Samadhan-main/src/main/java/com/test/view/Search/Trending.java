package com.test.view.Search;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.test.dao.ProfileDetailData;
import com.test.model.Post;
import com.test.servies.InitializeFirbase;
import com.test.view.Home.HomeMain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

public class Trending {

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Trending.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Trending.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox getTrendingContentOnly() {
        ListView<Post> trendingListView = new ListView<>();
        
        trendingListView.setStyle("-fx-background-color: black; -fx-control-inner-background: black;");
        trendingListView.setCellFactory(lv -> new HomeMain().new PostListCell());

        Platform.runLater(() -> trendingListView.lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0)));

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(60, 60);

        StackPane stackPane = new StackPane(trendingListView, loadingIndicator);
        stackPane.setPadding(new Insets(20, 0, 0, 0));

        Task<ObservableList<Post>> fetchTrendingTask = new Task<>() {
            @Override
            protected ObservableList<Post> call() throws Exception {
                String currentUserId = ProfileDetailData.getUID();
                ObservableList<Post> trendingPosts = FXCollections.observableArrayList();
                ApiFuture<QuerySnapshot> future = InitializeFirbase.getdb()
                        .collection("posts")
                        .orderBy("likeCount", Query.Direction.DESCENDING)
                        .limit(50)
                        .get();
                QuerySnapshot querySnapshot = future.get();
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String formattedDate = document.getTimestamp("timestamp") != null ? sdf.format(document.getTimestamp("timestamp").toDate()) : "No date";
                    
                    String postId = document.getId(); 

                    Post post = new Post(
                            postId,
                            document.getString("username"),
                            document.getString("description"),
                            document.getString("imageUrl"),
                            document.getString("location"),
                            formattedDate,
                            document.getString("profileImage"),
                            document.contains("likeCount") ? document.getLong("likeCount").intValue() : 0
                    );
                    
                
                    boolean isLikedInDb = false;
                    if (currentUserId != null && document.contains("likes") && document.get("likes") instanceof Map) {
                        Map<String, Object> likesMap = (Map<String, Object>) document.get("likes");
                        if (likesMap.containsKey(currentUserId)) {
                            isLikedInDb = true;
                        }
                    }

                    if (HomeMain.sessionLikedPostIds.contains(postId)) {
                        post.setLikedByCurrentUser(true);
                    } else if (HomeMain.sessionUnlikedPostIds.contains(postId)) {
                        post.setLikedByCurrentUser(false);
                    } else {
                        post.setLikedByCurrentUser(isLikedInDb);
                    }

                    trendingPosts.add(post);
                }
                return trendingPosts;
            }
        };

        fetchTrendingTask.setOnSucceeded(event -> {
            loadingIndicator.setVisible(false);
            trendingListView.setItems(fetchTrendingTask.getValue());
        });

        fetchTrendingTask.setOnFailed(event -> {
            loadingIndicator.setVisible(false);
            fetchTrendingTask.getException().printStackTrace();
            Label errorLabel = new Label("Error: Could not load trending posts.");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-family: '" + FONT_FAMILY + "';");
            stackPane.getChildren().add(errorLabel);
        });

        new Thread(fetchTrendingTask).start();

        VBox trendingContentContainer = new VBox(stackPane);
        trendingContentContainer.setAlignment(Pos.CENTER);

        return trendingContentContainer;
    }
}