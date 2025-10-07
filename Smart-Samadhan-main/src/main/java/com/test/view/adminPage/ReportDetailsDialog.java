package com.test.view.adminPage;

import com.test.model.Request;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReportDetailsDialog extends Dialog<Void> {

    private static final String FONT_FAMILY = "Poppins";

    public ReportDetailsDialog(Request request) {
        // --- Dialog Styling ---
        getDialogPane().setStyle("-fx-background-color: #1a1a1a;");

        // --- FIXED DIALOG SIZE ---
        // Set the preferred, minimum, and maximum size to the same values
        getDialogPane().setPrefSize(600, 750);
        getDialogPane().setMinSize(600, 750);
        getDialogPane().setMaxSize(600, 750);

        setTitle("Report Details: " + request.getTitle());
        setHeaderText("Reviewing report submitted by " + request.getFirstName() + " " + request.getLastName());

        getDialogPane().lookup(".header-panel").setStyle("-fx-background-color: #1a1a1a;");
        Label headerLabel = (Label) getDialogPane().lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '"
                    + FONT_FAMILY + "';");
            headerLabel.setWrapText(true);
        }

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
        if (closeButton != null) {
            closeButton.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY
                    + "'; -fx-font-weight: bold;");
        }

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(10));

        // //--- Image Viewer Section ---
        // ImageView imageView = new ImageView();
        // try {
        // // Background loading is enabled with the second parameter (true)
        // Image image = new Image(request.getImageUrl(), true);
        // imageView.setImage(image);
        // System.out.println(image);
        // } catch (Exception e) {
        // System.err.println("Failed to load image: " + request.getImageUrl());
        // }

        ImageView imageView = new ImageView();
        try {
            String urlFromDatabase = request.getImageUrl();
            System.out.println("JavaFX is trying to load this URL: " + urlFromDatabase);

            Image image = new Image(urlFromDatabase, true); // true for background loading
            imageView.setImage(image);

            // This listener will tell us if the image actually loaded or failed
            image.errorProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    System.err
                            .println("!!! JavaFX FAILED to load the image. Check for firewall or certificate issues.");
                    image.getException().printStackTrace();
                }
            });

            System.out.println("Image object created. Waiting for it to load...");

        } catch (Exception e) {
            System.err.println("An unexpected error occurred while creating the Image object.");
            e.printStackTrace();
        }

        imageView.setFitHeight(300);
        imageView.setFitWidth(550);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // --- Details Grid ---
        GridPane grid = createDetailsGrid(request);

        // Add image and grid to the main layout
        mainLayout.getChildren().addAll(imageView, grid);

        getDialogPane().setContent(mainLayout);
    }

    private GridPane createDetailsGrid(Request request) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));

        grid.add(styleLabel(new Label("Applicant:")), 0, 0);
        grid.add(styleText(new Text(request.getFirstName() + " " + request.getLastName())), 1, 0, 2, 1);

        grid.add(styleLabel(new Label("Contact Email:")), 0, 1);
        grid.add(styleText(new Text(request.getEmail())), 1, 1, 2, 1);

        grid.add(styleLabel(new Label("Contact Phone:")), 0, 2);
        grid.add(styleText(new Text(request.getPhone())), 1, 2, 2, 1);

        grid.add(styleLabel(new Label("Submitted On:")), 0, 3);
        grid.add(styleText(new Text(request.submittedDateProperty().get())), 1, 3, 2, 1);

        grid.add(styleLabel(new Label("Location:")), 0, 4);
        grid.add(styleText(new Text(request.getLocation())), 1, 4);

        Button mapsButton = new Button("Open in Maps");
        mapsButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY
                + "'; -fx-font-weight: bold; -fx-background-radius: 5;");
        mapsButton.setOnAction(e -> openLocationInMaps(request.getLocation()));
        grid.add(mapsButton, 2, 4);

        grid.add(styleLabel(new Label("Issue Type:")), 0, 5);
        grid.add(styleText(new Text(request.getIssueType())), 1, 5, 2, 1);

        grid.add(styleLabel(new Label("Description:")), 0, 6);
        Text descText = styleText(new Text(request.getDescription()));
        descText.setWrappingWidth(400);
        grid.add(descText, 1, 6, 2, 1);

        return grid;
    }

    private Label styleLabel(Label label) {
        label.setStyle("-fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY
                + "'; -fx-font-size: 14px; -fx-font-weight: bold;");
        return label;
    }

    private Text styleText(Text text) {
        text.setFont(Font.font(FONT_FAMILY, 14));
        text.setFill(Color.LIGHTGRAY);
        return text;
    }

    private void openLocationInMaps(String location) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
                URI mapUri = new URI("https://www.google.com/maps/search/?api=1&query=" + encodedLocation);
                Desktop.getDesktop().browse(mapUri);
            } catch (Exception ex) {
                System.err.println("Failed to open maps link.");
                ex.printStackTrace();
            }
        } else {
            System.err.println("Desktop browsing not supported on this platform.");
        }
    }
}