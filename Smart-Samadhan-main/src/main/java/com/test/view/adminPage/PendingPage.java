package com.test.view.adminPage;

import com.test.dao.ReportDAO;
import com.test.model.Request;
import com.test.servies.FirestoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class PendingPage {

    private TableView<Request> table;
    private Text totalPendingText, highPriorityText, underReviewText, newRequestsText;
    private Label tablePlaceholder;
    private final ReportDAO reportDAO = new ReportDAO();
    private final ObservableList<Request> allPendingReports = FXCollections.observableArrayList();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(PendingPage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(PendingPage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createScenePending() {
        table = createRequestTable();
        totalPendingText = new Text("0");
        highPriorityText = new Text("0");
        underReviewText = new Text("0");
        newRequestsText = new Text("0");

        VBox totalVb = createOriginalSummaryBox("Total Pending", totalPendingText);
        VBox priorityVb = createOriginalSummaryBox("High Priority", highPriorityText);
        VBox underVb = createOriginalSummaryBox("Under Review", underReviewText);
        VBox newRequestVb = createOriginalSummaryBox("New Requests", newRequestsText);

        totalVb.setOnMouseClicked(e -> table.setItems(allPendingReports));
        newRequestVb.setOnMouseClicked(e -> filterTableByStatus("Pending"));
        underVb.setOnMouseClicked(e -> filterTableByStatus("Under Review"));
        priorityVb.setOnMouseClicked(e -> filterTableByPriority("High"));

        HBox parentHBox1 = new HBox(20, totalVb, priorityVb);
        HBox parentHBox2 = new HBox(20, underVb, newRequestVb);

        refreshTableData();

        VBox mainLayout = new VBox(20, parentHBox1, parentHBox2, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 50; -fx-background-radius: 35;");
        return mainLayout;
    }
    
    private void filterTableByStatus(String status) {
        ObservableList<Request> filtered = allPendingReports.stream()
                .filter(r -> status.equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filtered);
    }

    private void filterTableByPriority(String priority) {
        ObservableList<Request> filtered = allPendingReports.stream()
                .filter(r -> priority.equalsIgnoreCase(r.getPriority()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filtered);
    }

    private void refreshTableData() {
        if (tablePlaceholder != null) tablePlaceholder.setText("Loading reports...");
        if (table != null) table.setItems(FXCollections.observableArrayList());

        Task<ObservableList<Request>> fetchTask = FirestoreService.createFetchReportsTask();

        fetchTask.setOnSucceeded(e -> {
            ObservableList<Request> allReports = fetchTask.getValue();
            this.allPendingReports.clear();
            this.allPendingReports.addAll(allReports.stream()
                    .filter(r -> r.getStatus() != null &&
                            (r.getStatus().equalsIgnoreCase("Pending") || r.getStatus().equalsIgnoreCase("Under Review")))
                    .collect(Collectors.toList()));

            table.setItems(this.allPendingReports);

            if (this.allPendingReports.isEmpty() && tablePlaceholder != null) {
                tablePlaceholder.setText("No pending reports found.");
            }
            updateSummaryCounts(this.allPendingReports);
        });

        fetchTask.setOnFailed(e -> {
            if (tablePlaceholder != null) tablePlaceholder.setText("Failed to load reports. Check console for errors.");
            if (fetchTask.getException() != null) fetchTask.getException().printStackTrace();
        });

        new Thread(fetchTask).start();
    }

    private void updateSummaryCounts(ObservableList<Request> pendingReports) {
        totalPendingText.setText(String.valueOf(pendingReports.size()));
        highPriorityText.setText(String.valueOf(pendingReports.stream().filter(r -> "High".equalsIgnoreCase(r.getPriority())).count()));
        underReviewText.setText(String.valueOf(pendingReports.stream().filter(r -> "Under Review".equalsIgnoreCase(r.getStatus())).count()));
        newRequestsText.setText(String.valueOf(pendingReports.stream().filter(r -> "Pending".equalsIgnoreCase(r.getStatus())).count()));
    }

    private TableView<Request> createRequestTable() {
        TableView<Request> newTable = new TableView<>();
        tablePlaceholder = new Label("Initializing...");
        tablePlaceholder.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px;");
        newTable.setPlaceholder(tablePlaceholder);
        newTable.setEditable(true);
        newTable.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: white; -fx-border-color: #dcdcdc; -fx-border-radius: 8; -fx-padding: 1;");

        // --- COLUMNS ---
        TableColumn<Request, String> colRequest = new TableColumn<>("Request Details");
        colRequest.setCellValueFactory(data -> data.getValue().titleProperty());
        colRequest.setEditable(false);

        TableColumn<Request, String> colApplicant = new TableColumn<>("Applicant");
        colApplicant.setCellValueFactory(data -> data.getValue().applicantProperty());
        colApplicant.setEditable(false);

        TableColumn<Request, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(data -> data.getValue().locationProperty());
        colLocation.setEditable(false);

        TableColumn<Request, String> colPriority = new TableColumn<>("Priority");
        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        ObservableList<String> priorityOptions = FXCollections.observableArrayList("Low", "Medium", "High");
        colPriority.setCellFactory(ComboBoxTableCell.forTableColumn(priorityOptions));
        colPriority.setOnEditCommit(event -> {
            Request request = event.getRowValue();
            request.setPriority(event.getNewValue());
            reportDAO.updateReportPriority(request.getReportId(), event.getNewValue());
            updateSummaryCounts(allPendingReports);
        });

        TableColumn<Request, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Pending", "Under Review", "Completed");
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptions));
        colStatus.setOnEditCommit(event -> {
            Request request = event.getRowValue();
            String newStatus = event.getNewValue();
            reportDAO.updateReportStatus(request.getReportId(), newStatus);
            if ("Completed".equalsIgnoreCase(newStatus) || "Rejected".equalsIgnoreCase(newStatus)) {
                table.getItems().remove(request);
                allPendingReports.remove(request);
            } else {
                request.setStatus(newStatus);
            }
            updateSummaryCounts(allPendingReports);
        });

        TableColumn<Request, String> colSubmitted = new TableColumn<>("Submitted");
        colSubmitted.setCellValueFactory(data -> data.getValue().submittedDateProperty());
        colSubmitted.setEditable(false);

        TableColumn<Request, String> colAction = new TableColumn<>("Action");
        ObservableList<String> actionOptions = FXCollections.observableArrayList("Accept", "Reject");
        colAction.setCellFactory(ComboBoxTableCell.forTableColumn(actionOptions));
        colAction.setOnEditCommit(event -> {
            Request request = event.getRowValue();
            String action = event.getNewValue();
            String newStatus = "Accept".equalsIgnoreCase(action) ? "Completed" : "Rejected";
            reportDAO.updateReportStatus(request.getReportId(), newStatus);
            table.getItems().remove(request);
            allPendingReports.remove(request);
            updateSummaryCounts(allPendingReports);
        });


        TableColumn<Request, Void> colDetails = new TableColumn<>("Details");
        colDetails.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                viewButton.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    ReportDetailsDialog dialog = new ReportDetailsDialog(request);
                    dialog.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });

        newTable.getColumns().addAll(colRequest, colApplicant, colLocation, colPriority, colStatus, colSubmitted, colAction, colDetails);
        newTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return newTable;
    }

    private VBox createOriginalSummaryBox(String title, Text countTextNode) {
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        titleText.setTranslateX(130);

        countTextNode.setStyle("-fx-font-size: 20; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        countTextNode.setTranslateX(190);

        VBox vbox = new VBox(5, titleText, countTextNode);
        vbox.setPadding(new Insets(15, 0, 0, 0));

        String defaultStyle = "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 20;";
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
            vbox.setCursor(Cursor.HAND);
        });
        vbox.setOnMouseExited(e -> {
            vbox.setEffect(null);
            vbox.setStyle(defaultStyle);
            vbox.setCursor(Cursor.DEFAULT);
        });

        return vbox;
    }
}