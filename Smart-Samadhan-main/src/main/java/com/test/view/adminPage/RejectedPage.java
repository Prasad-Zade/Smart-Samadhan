package com.test.view.adminPage;

import com.test.model.Request;
import com.test.servies.FirestoreService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

public class RejectedPage {

    private TableView<Request> table;
    private Text totalRejectedText;
    private Label tablePlaceholder;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(RejectedPage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(RejectedPage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createSceneRejected() {
        table = createRejectedTable();
        totalRejectedText = new Text("0");

        VBox totalVb = createSummaryBox("Total Rejected", totalRejectedText);
        HBox summaryHBox = new HBox(totalVb);

        refreshTableData();

        VBox mainLayout = new VBox(20, summaryHBox, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 50; -fx-background-radius: 35;");
        return mainLayout;
    }

    private void refreshTableData() {
        if (tablePlaceholder != null) {
            tablePlaceholder.setText("Loading reports...");
        }
        if (table != null) {
            table.setItems(FXCollections.observableArrayList());
        }

        Task<ObservableList<Request>> fetchTask = FirestoreService.createFetchReportsTask();

        fetchTask.setOnSucceeded(e -> {
            ObservableList<Request> allReports = fetchTask.getValue();
            ObservableList<Request> rejectedReports = allReports.stream()
                    .filter(r -> r.getStatus() != null && r.getStatus().equalsIgnoreCase("Rejected"))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            table.setItems(rejectedReports);

            if (rejectedReports.isEmpty() && tablePlaceholder != null) {
                tablePlaceholder.setText("No rejected reports found.");
            }

            totalRejectedText.setText(String.valueOf(rejectedReports.size()));
        });

        fetchTask.setOnFailed(e -> {
            if (tablePlaceholder != null) {
                tablePlaceholder.setText("Failed to load reports. Check console for errors.");
            }
            if (fetchTask.getException() != null) {
                fetchTask.getException().printStackTrace();
            }
        });

        new Thread(fetchTask).start();
    }

    private TableView<Request> createRejectedTable() {
        TableView<Request> newTable = new TableView<>();
        tablePlaceholder = new Label("Initializing...");
        tablePlaceholder.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px;");
        newTable.setPlaceholder(tablePlaceholder);
        newTable.setEditable(false);
        newTable.setStyle(
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: white;" +
            "-fx-border-color: #dcdcdc;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 1;"
        );

        TableColumn<Request, String> colRequest = new TableColumn<>("Request Details");
        colRequest.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Request, String> colApplicant = new TableColumn<>("Applicant");
        colApplicant.setCellValueFactory(data -> data.getValue().applicantProperty());

        TableColumn<Request, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));

        TableColumn<Request, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());

        TableColumn<Request, String> colSubmitted = new TableColumn<>("Submitted");
        colSubmitted.setCellValueFactory(data -> data.getValue().submittedDateProperty());

        newTable.getColumns().addAll(colRequest, colApplicant, colLocation, colStatus, colSubmitted);
        newTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return newTable;
    }

    private VBox createSummaryBox(String title, Text countTextNode) {
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        titleText.setTranslateX(130);

        countTextNode.setStyle("-fx-font-size: 20; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        countTextNode.setTranslateX(190);

        VBox vbox = new VBox(5, titleText, countTextNode);
        vbox.setPadding(new Insets(15, 0, 0, 0));

        String defaultStyle = "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #c31432, #c31432 ); -fx-background-radius: 20;";
        vbox.setStyle(defaultStyle);

        vbox.setPrefHeight(90);
        vbox.setMinWidth(400);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.BLACK);

        vbox.setOnMouseEntered(e -> {
            vbox.setEffect(dropShadow);
            vbox.setStyle(defaultStyle + "-fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        vbox.setOnMouseExited(e -> {
            vbox.setEffect(null);
            vbox.setStyle(defaultStyle);
        });

        return vbox;
    }
}