package com.test.view.adminPage;

import com.test.model.Request;
import com.test.servies.FirestoreService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.stream.Collectors;

public class Dashboard {

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(Dashboard.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(Dashboard.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createScene() {
        Label loadingLabel = new Label("Loading Dashboard Data...");
        loadingLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
        VBox container = new VBox(loadingLabel);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 50; -fx-background-radius: 35;");
        container.setMinHeight(600);
        container.setMaxWidth(900);

        Task<ObservableList<Request>> fetchTask = FirestoreService.createFetchReportsTask();

        fetchTask.setOnSucceeded(e -> {
            ObservableList<Request> allReports = fetchTask.getValue();
            VBox content = buildDashboardContent(allReports);
            container.getChildren().setAll(content);
        });

        fetchTask.setOnFailed(e -> {
            loadingLabel.setText("Failed to load dashboard. Check console for errors.");
            if (fetchTask.getException() != null) {
                fetchTask.getException().printStackTrace();
            }
        });

        new Thread(fetchTask).start();

        return container;
    }

    private VBox buildDashboardContent(ObservableList<Request> allReports) {
        long totalCount = allReports.size();
        long pendingCount = allReports.stream().filter(r -> r.getStatus() != null && (r.getStatus().equalsIgnoreCase("Pending") || r.getStatus().equalsIgnoreCase("Under Review"))).count();
        long resolvedCount = allReports.stream().filter(r -> r.getStatus() != null && (r.getStatus().equalsIgnoreCase("Approved") || r.getStatus().equalsIgnoreCase("Completed"))).count();

        Text adminName = new Text("Admin Panel");
        adminName.setStyle("-fx-font-size: 30; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: #007bff;-fx-font-weight:bold");

        VBox totalVb = createSummaryBox("Total Issues", String.valueOf(totalCount));
        VBox pendingVb = createSummaryBox("Pending", String.valueOf(pendingCount));
        VBox resolvedVb = createSummaryBox("Resolved", String.valueOf(resolvedCount));
        HBox parentHBox = new HBox(80, totalVb, pendingVb, resolvedVb);

        Map<String, Long> statusCounts = allReports.stream()
                .filter(r -> r.getStatus() != null && !r.getStatus().isEmpty())
                .collect(Collectors.groupingBy(Request::getStatus, Collectors.counting()));
        PieChart statusChart = createPieChart(null, statusCounts);
        Text title = new Text("Issue by Status");
        title.setStyle("-fx-fill: white; -fx-font-size:18; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");

        Map<String, Long> priorityCounts = allReports.stream()
                .filter(r -> r.getPriority() != null && !r.getPriority().isEmpty())
                .collect(Collectors.groupingBy(Request::getPriority, Collectors.counting()));
        PieChart priorityChart = createPieChart(null, priorityCounts);
        Text status = new Text("Issue by Priority");
        status.setStyle("-fx-fill: white; -fx-font-size:18; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");

        HBox titles = new HBox(300, title, status);
        titles.setTranslateX(130);
        HBox chartHBox = new HBox(20, statusChart, priorityChart);

        return new VBox(20, adminName, parentHBox, titles, chartHBox);
    }

    private VBox createSummaryBox(String title, String count) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 20));
        titleText.setFill(Color.WHITE);

        Text countText = new Text(count);
        countText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        countText.setFill(Color.WHITE);

        VBox vbox = new VBox(10, titleText, countText);
        vbox.setAlignment(Pos.CENTER);
        
        String defaultStyle = "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff); -fx-background-radius: 20;";
        vbox.setStyle(defaultStyle);
        
        vbox.setPrefHeight(120);
        vbox.setMinWidth(220);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.5));

        vbox.setOnMouseEntered(e -> {
            vbox.setEffect(dropShadow);
            vbox.setStyle(defaultStyle + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });

        vbox.setOnMouseExited(e -> {
            vbox.setEffect(null);
            vbox.setStyle(defaultStyle);
        });
        
        return vbox;
    }

    private PieChart createPieChart(String title, Map<String, Long> dataMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        dataMap.forEach((key, value) -> pieChartData.add(new PieChart.Data(key + " (" + value + ")", value)));

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No Data Available", 1));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(title);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefSize(420, 420);
        pieChart.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-family: '" + FONT_FAMILY + "';");

        Platform.runLater(() -> {
            Node titleNode = pieChart.lookup(".chart-title");
            if (titleNode instanceof Text) {
                ((Text) titleNode).setFill(Color.WHITE);
                ((Text) titleNode).setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
            }

            for (Node node : pieChart.lookupAll(".chart-pie-label")) {
                if (node instanceof Text) {
                    ((Text) node).setFill(Color.WHITE);
                    ((Text) node).setFont(Font.font(FONT_FAMILY, 12));
                }
            }
        });

        return pieChart;
    }
}