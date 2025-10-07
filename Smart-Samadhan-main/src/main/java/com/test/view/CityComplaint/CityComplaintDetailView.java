package com.test.view.CityComplaint;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CityComplaintDetailView {
    private VBox root;
    private static final String FONT_FAMILY = "Poppins";

    public CityComplaintDetailView(String displayCityName, BorderPane mainPane) {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);" +
            "-fx-background-radius: 35;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;"
        );

        String buttonStyle = "-fx-background-radius: 15; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);";

        // 🔙 Back Button
        Button backButton = new Button("⬅ Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> {
            CityComplaintListView listView = new CityComplaintListView(mainPane);
            mainPane.setCenter(listView.getView());
        });

        // Title
        Label title = new Label("📝 Complaints in " + displayCityName);
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Header
        VBox header = new VBox(10, backButton, title);
        root.getChildren().add(header);

        // Fetch Firestore data
        Firestore db = FirestoreClient.getFirestore();
        String normalizedCityKey = displayCityName.trim().toLowerCase();

        try {
            // reports
            ApiFuture<QuerySnapshot> reportsFuture = db.collection("reports").get();
            List<QueryDocumentSnapshot> reportDocs = reportsFuture.get().getDocuments();
            for (QueryDocumentSnapshot doc : reportDocs) {
                String location = doc.getString("location");
                if (location != null && location.trim().toLowerCase().equals(normalizedCityKey)) {
                    String desc = doc.getString("description");
                    root.getChildren().add(createCard(desc));
                }
            }

            // posts
            ApiFuture<QuerySnapshot> postsFuture = db.collection("posts").get();
            List<QueryDocumentSnapshot> postDocs = postsFuture.get().getDocuments();
            for (QueryDocumentSnapshot doc : postDocs) {
                String location = doc.getString("location");
                if (location != null && location.trim().toLowerCase().equals(normalizedCityKey)) {
                    String desc = doc.getString("description");
                    root.getChildren().add(createCard(desc));
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private VBox createCard(String description) {
        VBox card = new VBox();
        card.setPadding(new Insets(10));
        card.setSpacing(5);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;"
        );

        Label descLabel = new Label("📌 " + description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");

        card.getChildren().add(descLabel);
        return card;
    }

    public Parent getView() {
        return root;
    }
}
