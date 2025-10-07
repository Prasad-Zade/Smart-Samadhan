package com.test.view.ReportIssue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.test.Controller.PostSceneController;
import com.test.dao.ProfileDetailData;
import com.test.dao.ReportDAO;
import com.test.util.LanguageManager;
import com.test.view.LoginPage.Login;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReportIssue {
    public File imageFile;
    public String userEmail;

    private static final String FONT_FAMILY = "Poppins";
    private final LanguageManager lang = LanguageManager.getInstance();

    static {
        try {
            Font.loadFont(ReportIssue.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ReportIssue.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public Scene createReportScene(Runnable back, File imageFile, String userEmail) {
        ImageView reportIcon = new ImageView(new Image("assets/Images/document-icon-graphic-ry6hxlmiuy2qzh2g-2.jpg"));
        reportIcon.setFitHeight(40);
        reportIcon.setFitWidth(40);
        this.imageFile = imageFile;
        this.userEmail = userEmail;

        Label reportLabel = new Label(lang.getString("report.title"));
        reportLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 28px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-text-fill: #00c6ff;");

        HBox heading = new HBox(15, reportIcon, reportLabel);
        heading.setAlignment(Pos.CENTER);
        heading.setPadding(new Insets(20));

        String fieldStyle = "-fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 10; -fx-control-inner-background: #2e2e2e; -fx-text-fill: white;";
        String labelStyle = "-fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;";
        String sectionStyle = "-fx-font-size: 20px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold; -fx-fill: white;";

        Text personalInfo = new Text(lang.getString("report.personalInfo"));
        personalInfo.setStyle(sectionStyle);

        Text firstNameLabel = new Text(lang.getString("report.firstName"));
        firstNameLabel.setStyle(labelStyle);
        TextField firstNameField = new TextField();
        firstNameField.setText(ProfileDetailData.getFirstName());
        firstNameField.editableProperty().set(false);
        firstNameField.setStyle(fieldStyle);
        firstNameField.setFocusTraversable(false);

        Text lastNameLabel = new Text(lang.getString("report.lastName"));
        lastNameLabel.setStyle(labelStyle);
        TextField lastNameField = new TextField();
        lastNameField.setText(ProfileDetailData.getLastName());
        lastNameField.editableProperty().set(false);
        lastNameField.setStyle(fieldStyle);
        lastNameField.setFocusTraversable(false);

        HBox nameRow = new HBox(40, new VBox(5, firstNameLabel, firstNameField), new VBox(5, lastNameLabel, lastNameField));
        nameRow.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(nameRow.getChildren().get(1), Priority.ALWAYS);

        Text emailLabel = new Text(lang.getString("report.email"));
        emailLabel.setStyle(labelStyle);
        TextField emailField = new TextField();
        emailField.setText(Login.userEmail);
        emailField.setEditable(false);
        emailField.setStyle(fieldStyle);
        emailField.setFocusTraversable(false);

        Text phoneLabel = new Text(lang.getString("report.phone"));
        phoneLabel.setStyle(labelStyle);
        TextField phoneField = new TextField(ProfileDetailData.getphoneNumber());
        phoneField.setEditable(false);
        phoneField.setStyle(fieldStyle);
        phoneField.setFocusTraversable(false);

        HBox contactRow = new HBox(40,
                new VBox(5, emailLabel, emailField),
                new VBox(5, phoneLabel, phoneField));
        contactRow.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(contactRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(contactRow.getChildren().get(1), Priority.ALWAYS);

        Text reportInfo = new Text(lang.getString("report.reportDetails"));
        reportInfo.setStyle(sectionStyle);

        Text reportTitleLabel = new Text(lang.getString("report.reportTitle"));
        reportTitleLabel.setStyle(labelStyle);
        TextField reportTitleField = new TextField();
        reportTitleField.setStyle(fieldStyle);
        reportTitleField.setFocusTraversable(false);

        Text issueTypeLabel = new Text(lang.getString("report.issueType"));
        issueTypeLabel.setStyle(labelStyle);
        TextField issueTypeField = new TextField();
        issueTypeField.setStyle(fieldStyle);
        issueTypeField.setFocusTraversable(false);

        HBox issueRow = new HBox(40,
                new VBox(5, reportTitleLabel, reportTitleField),
                new VBox(5, issueTypeLabel, issueTypeField));
        issueRow.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(issueRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(issueRow.getChildren().get(1), Priority.ALWAYS);

        Text locationLabel = new Text(lang.getString("report.location"));
        locationLabel.setStyle(labelStyle);
        TextField locationField = new TextField();
        locationField.setStyle(fieldStyle);
        locationField.setFocusTraversable(false);

        Text dateLabel = new Text(lang.getString("report.date"));
        dateLabel.setStyle(labelStyle);
        TextField dateField = new TextField();
        dateField.setStyle(fieldStyle);
        dateField.setFocusTraversable(false);

        HBox locationRow = new HBox(40,
                new VBox(5, locationLabel, locationField),
                new VBox(5, dateLabel, dateField));
        locationRow.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(locationRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(locationRow.getChildren().get(1), Priority.ALWAYS);

        Text descLabel = new Text(lang.getString("report.description"));
        descLabel.setStyle(labelStyle);
        TextArea descArea = new TextArea();
        descArea.setWrapText(true);
        descArea.setPrefHeight(160);
        descArea.setStyle(fieldStyle);
        descArea.setMinHeight(300);
        StackPane descriptionPane = new StackPane(descArea);

        // MODIFIED - Button text is now from the language manager
        Button aiBtn = new Button(lang.getString("report.aiButton"));
        aiBtn.setStyle("-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);");


        aiBtn.setOnAction((ActionEvent arg0) -> {
            Rectangle shimmer = createShimmerRectangle(descArea);
            descriptionPane.getChildren().add(shimmer);
            TranslateTransition animation = createShimmerAnimation(shimmer, descArea.getMaxWidth());
            animation.play();

            Task<String> aiTask = new Task<>() {
                @Override
                protected String call() {
                    String locationText = locationField.getText();
                    String descriptionText = descArea.getText();
                    PostSceneController controller = new PostSceneController();
                    return controller.aiPrompString(locationText, descriptionText, null);
                }
            };

            aiTask.setOnSucceeded(event -> {
                descArea.setText(aiTask.getValue());
                animation.stop();
                descriptionPane.getChildren().remove(shimmer);
            });

            aiTask.setOnFailed(event -> {
                animation.stop();
                descriptionPane.getChildren().remove(shimmer);
                System.err.println("AI task failed: " + aiTask.getException().getMessage());
            });

            new Thread(aiTask).start();
        });

        ComboBox<String> languageCombo = new ComboBox<>();
        // MODIFIED - Language names are now from the language manager
        languageCombo.getItems().addAll(
            lang.getString("language.english"), 
            lang.getString("language.marathi"), 
            lang.getString("language.hindi"), 
            lang.getString("language.gujarati")
        );
        languageCombo.setPromptText(lang.getString("postScene.translatePrompt"));
        languageCombo.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);" +
            "-fx-mark-color: white;"
        );

        languageCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? lang.getString("postScene.translatePrompt") : item);
                setTextFill(Color.WHITE);
            }
        });

        Button languageButton = new Button(lang.getString("report.translateButton"));
        languageButton.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        languageButton.setOnAction(arg0 -> {
            String selected = languageCombo.getValue();
            if (selected != null) {
                Rectangle descShimmer = createShimmerRectangle(descriptionPane);
                descriptionPane.getChildren().add(descShimmer);
                TranslateTransition descAnimation = createShimmerAnimation(descShimmer, descriptionPane.getMaxWidth());
                descAnimation.play();
                
                String langCode = "en";
                if(selected.equals(lang.getString("language.marathi"))) langCode = "mr";
                else if(selected.equals(lang.getString("language.hindi"))) langCode = "hi";
                else if(selected.equals(lang.getString("language.gujarati"))) langCode = "gu";

                final String finalLangCode = langCode;

                Task<String[]> translateTask = new Task<>() {
                    @Override
                    protected String[] call() throws Exception {
                        String descriptionText = descArea.getText();
                        PostSceneController controller = new PostSceneController();
                        String translatedDescription = controller.translateText(descriptionText, finalLangCode);
                        return new String[]{translatedDescription};
                    }
                };

                translateTask.setOnSucceeded(event -> {
                    String[] translations = translateTask.getValue();
                    descArea.setText(translations[0]);
                    descAnimation.stop();
                    descriptionPane.getChildren().remove(descShimmer);
                });

                translateTask.setOnFailed(event -> {
                    descAnimation.stop();
                    descriptionPane.getChildren().remove(descShimmer);
                    System.err.println("Translation task failed: " + translateTask.getException().getMessage());
                });

                new Thread(translateTask).start();
            } else {
                showAlert("alert.noLanguageSelected.title", "alert.noLanguageSelected.message");
            }
        });

        Button backButton = new Button(lang.getString("report.backButton"));
        backButton.setOnAction(e -> back.run());

        Button submitButton = new Button(lang.getString("report.submitButton"));
        submitButton.setOnAction(e -> {
            if (firstNameField.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.firstNameEmpty.message"); return;
            }
            if (lastNameField.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.lastNameEmpty.message"); return;
            }
            if (!emailField.getText().trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                showAlert("alert.validationError.title", "alert.emailInvalid.message"); return;
            }
            if (!phoneField.getText().trim().matches("\\d{10}")) {
                showAlert("alert.validationError.title", "alert.phoneInvalid.message"); return;
            }
            if (reportTitleField.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.reportTitleEmpty.message"); return;
            }
            if (issueTypeField.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.issueTypeEmpty.message"); return;
            }
            if (locationField.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.locationEmpty.message"); return;
            }
            if (!dateField.getText().trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
                showAlert("alert.validationError.title", "alert.dateInvalid.message"); return;
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate reportDate = LocalDate.parse(dateField.getText().trim(), formatter);
                    if (reportDate.isAfter(LocalDate.now())) {
                        showAlert("alert.validationError.title", "alert.dateInFuture.message"); return;
                    }
                } catch (DateTimeParseException ex) {
                    showAlert("alert.validationError.title", "alert.dateParseException.message"); return;
                }
            }
            if (descArea.getText().trim().isEmpty()) {
                showAlert("alert.validationError.title", "alert.descriptionEmpty.message"); return;
            }

            submitButton.setDisable(true);
            submitButton.setText(lang.getString("report.submittingButton"));

            new Thread(() -> {
                try {
                    ReportDAO dao = new ReportDAO();
                    dao.createReport(userEmail, imageFile, firstNameField.getText(), lastNameField.getText(), emailField.getText(), phoneField.getText(),
                                     reportTitleField.getText(), issueTypeField.getText(), locationField.getText(), dateField.getText(), descArea.getText());
                    javafx.application.Platform.runLater(() -> {
                        submitButton.setDisable(false);
                        submitButton.setText(lang.getString("report.submitButton"));
                        showAlert("alert.success.title", "alert.success.message");
                        back.run();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        submitButton.setDisable(false);
                        submitButton.setText(lang.getString("report.submitButton"));
                        String errorMessage = String.format(lang.getString("alert.error.message"), ex.getMessage());
                        showAlert("alert.error.title", errorMessage, true);
                    });
                }
            }).start();
        });

        String buttonStyle = "-fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; " +
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);";
        backButton.setStyle(buttonStyle);
        submitButton.setStyle("-fx-background-color: #ff3b30; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 20;");

        backButton.setMinSize(120, 40);
        submitButton.setMinSize(150, 40);

        HBox buttonBox = new HBox(30, backButton, submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 40, 0));

        HBox ai = new HBox(10,aiBtn);
        HBox laBox = new HBox(1100, descLabel, ai);

        VBox formLayout = new VBox(25,
                personalInfo, nameRow, contactRow,
                reportInfo, issueRow, locationRow,
                laBox, descriptionPane);
        formLayout.setPadding(new Insets(20, 80, 10, 80));


        VBox mainLayout = new VBox(10, heading, formLayout, buttonBox);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);");
        mainLayout.setPadding(new Insets(20));

        ScrollPane main = new ScrollPane(mainLayout);
        main.setStyle("""
            -fx-background-color: transparent;
            -fx-background: transparent;
            -fx-control-inner-background: transparent;
            -fx-padding: 0;
        """);
        main.setFitToWidth(true);
        main.setFitToHeight(true);

        return new Scene(main, 1550, 800);
    }

    private void showAlert(String titleKey, String messageKey) {
        showAlert(titleKey, lang.getString(messageKey), false);
    }
    
    private void showAlert(String titleKey, String message, boolean isRawMessage) {
        Stage alertStage = new Stage();
        alertStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(lang.getString(titleKey));
        titleText.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white; -fx-font-weight: bold;");

        Text messageText = new Text(isRawMessage ? message : lang.getString(message));
        messageText.setStyle("-fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");

        Button closeButton = new Button(lang.getString("alert.okButton"));
        closeButton.setStyle(
            "-fx-background-radius: 15;" +
            "-fx-font-size: 13px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-text-fill: white;" +
            "-fx-padding: 5 20 5 20;" +
            "-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff);"
        );

        closeButton.setOnAction(e -> alertStage.close());

        VBox alertBox = new VBox(15, titleText, messageText, closeButton);
        alertBox.setStyle("-fx-background-color: #2e2e2e; -fx-background-radius: 25; -fx-padding: 25;");
        alertBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(alertBox);
        scene.setFill(Color.TRANSPARENT);

        alertStage.setScene(scene);
        alertStage.setWidth(350);
        alertStage.setHeight(200);
        alertStage.show();
    }

    private Rectangle createShimmerRectangle(TextArea textArea) {
        Rectangle shimmer = new Rectangle(textArea.getWidth(), textArea.getHeight());
        shimmer.setFill(new Color(1, 1, 1, 0.2));
        shimmer.setArcWidth(10);
        shimmer.setArcHeight(10);
        return shimmer;
    }

    private TranslateTransition createShimmerAnimation(Rectangle shimmer, double width) {
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(1.5), shimmer);
        transition.setFromX(-width);
        transition.setToX(width);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        return transition;
    }

    private Rectangle createShimmerRectangle(Region nodeToCover) {
        Rectangle shimmerRect = new Rectangle();
        shimmerRect.widthProperty().bind(nodeToCover.widthProperty());
        shimmerRect.heightProperty().bind(nodeToCover.heightProperty());

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.TRANSPARENT),
            new Stop(0.4, Color.rgb(255, 255, 255, 0.15)),
            new Stop(0.5, Color.rgb(255, 255, 255, 0.3)),
            new Stop(0.6, Color.rgb(255, 255, 255, 0.15)),
            new Stop(1, Color.TRANSPARENT)
        );
        shimmerRect.setFill(gradient);
        return shimmerRect;
    }
}