package com.test.view.CityComplaint;

import com.test.Controller.CityComplaintController;
import com.test.model.CityComplaintModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;

public class CityComplaintListView {
    private ScrollPane scrollPane;
    private VBox root;
    private BorderPane mainPane;

    public CityComplaintListView(BorderPane mainPane) {
        this.mainPane = mainPane;

        root = new VBox(15);
        root.setPadding(new Insets(20));

        CityComplaintController controller = new CityComplaintController();
        List<CityComplaintModel> cityList = controller.getCityComplaintData();
        cityList.sort((a, b) -> Integer.compare(b.getComplaintCount(), a.getComplaintCount()));

        for (CityComplaintModel model : cityList) {
            VBox card = createCard(model);
            root.getChildren().add(card);
        }

        scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 0;"
        );

        scrollPane.setPadding(new Insets(30));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private VBox createCard(CityComplaintModel model) {
        VBox card = new VBox();
        card.setPadding(new Insets(10));
        card.setSpacing(10);
        card.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-border-radius: 12; -fx-background-radius: 12;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label cityLabel = new Label("📍 " + model.getCityName());
        cityLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label countLabel = new Label("📝 Complaints: " + model.getComplaintCount());
        countLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007bff;");

        card.getChildren().addAll(cityLabel, countLabel);

        card.setOnMouseClicked((MouseEvent e) -> {
            CityComplaintDetailView detailView = new CityComplaintDetailView(model.getCityName(),mainPane);
            mainPane.setCenter(detailView.getView());
        });

        return card;
    }

    public Parent getView() {
        return scrollPane;
    }
}
