package com.test.view.Search;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Location {

    // This method returns a VBox with scrollable trending section
    public VBox getTrendingContentOnly() {
        // Trending Title
        Text trending = new Text("Trending");
        trending.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: white;");
        trending.setTranslateY(-30);

        // Grid of trending posts/items
        GridPane postGridPane = new GridPane();
        postGridPane.setHgap(20);
        postGridPane.setVgap(20);

        // Add 100 sample items
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                Text text = new Text("Core2Web");
                text.setStyle("-fx-fill: white; -fx-font-size: 16px;");
                postGridPane.add(text, j, i);
            }
        }

        // VBox containing the title and grid
        VBox trendingVB = new VBox(20, trending, postGridPane);
        trendingVB.setPadding(new Insets(30, 0, 10, 0));
        trendingVB.setAlignment(Pos.TOP_CENTER);
        trendingVB.setStyle("-fx-background-color: black;");

        // Scroll container
        ScrollPane scrollPane = new ScrollPane(trendingVB);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");
        scrollPane.setMaxHeight(700);
        scrollPane.setMaxWidth(800);

        // Final VBox to return (e.g., to be set at center of BorderPane)
        VBox main = new VBox(scrollPane);
        main.setStyle("-fx-background-color: black;");

        return main;
    }
}
