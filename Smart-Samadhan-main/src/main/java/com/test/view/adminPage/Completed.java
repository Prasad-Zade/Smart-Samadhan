package com.test.view.adminPage;

import com.test.dao.ReportDAO;
import com.test.model.Request;
import com.test.servies.FirestoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

public class Completed {

    private TableView<Request> table;
    private Label tablePlaceholder;
    private Text title;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Completed.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Completed.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createSceneCompleted() {
        title = new Text("Completed Reports");
        title.setStyle("-fx-font-size: 30; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: #007bff;");

        table = createRequestTable();
        configureTableContextMenu();
        refreshTableData();

        VBox layout = new VBox(20, title, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 50; -fx-background-radius: 35;");
        layout.setPadding(new Insets(30));
        layout.setMinHeight(600);
        layout.setMaxWidth(900);
        return layout;
    }

    private void refreshTableData() {
        tablePlaceholder.setText("Loading completed reports...");
        table.setItems(FXCollections.observableArrayList());

        Task<ObservableList<Request>> fetchTask = FirestoreService.createFetchReportsTask();

        fetchTask.setOnSucceeded(e -> {
            ObservableList<Request> allReports = fetchTask.getValue();
            ObservableList<Request> completedReports = allReports.stream()
                    .filter(r -> r.getStatus() != null &&
                            (r.getStatus().equalsIgnoreCase("Approved") || r.getStatus().equalsIgnoreCase("Completed")))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            table.setItems(completedReports);
            tablePlaceholder.setText("No completed reports found.");
            title.setText("Completed Reports (" + completedReports.size() + ")");
        });

        fetchTask.setOnFailed(e -> {
            tablePlaceholder.setText("Failed to load reports. Check console.");
            fetchTask.getException().printStackTrace();
        });

        new Thread(fetchTask).start();
    }

    private void configureTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        ReportDAO reportDAO = new ReportDAO();

        MenuItem markPending = new MenuItem("Re-open as Pending");
        markPending.setStyle("-fx-font-family: '" + FONT_FAMILY + "';");
        markPending.setOnAction(e -> {
            Request selectedReport = table.getSelectionModel().getSelectedItem();
            if (selectedReport != null) {
                reportDAO.updateReportStatus(selectedReport.getReportId(), "Pending");
                refreshTableData();
            }
        });

        contextMenu.getItems().add(markPending);

        table.setRowFactory(tv -> {
            TableRow<Request> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    private TableView<Request> createRequestTable() {
        TableView<Request> newTable = new TableView<>();
        tablePlaceholder = new Label("Initializing...");
        tablePlaceholder.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px;");
        newTable.setPlaceholder(tablePlaceholder);
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

        TableColumn<Request, String> colPriority = new TableColumn<>("Priority");
        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());

        TableColumn<Request, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());

        TableColumn<Request, String> colSubmitted = new TableColumn<>("Submitted");
        colSubmitted.setCellValueFactory(data -> data.getValue().submittedDateProperty());

        newTable.getColumns().addAll(colRequest, colApplicant, colPriority, colStatus, colSubmitted);
        newTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return newTable;
    }
}