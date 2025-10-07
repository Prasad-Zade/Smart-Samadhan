package com.test.view.LoginPage;

import java.util.Map;
import com.test.Controller.SignUpControl;
import com.test.dao.SignUpData;
import com.test.model.SignUpModel;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignUp {
    public static String email;
    public static String username;

    private Scene p2Scene;
    private Stage p2Stage;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(SignUp.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(SignUp.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setScene(Scene scene) {
        this.p2Scene = scene;
    }

    public void setStage(Stage stage) {
        this.p2Stage = stage;
    }

    public StackPane createScene(Runnable back) {

        Label createAccount = new Label("CREATE ACCOUNT");
        createAccount.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        createAccount.setStyle("-fx-text-fill: #007bff;");

        Text userName = new Text("UserName");
        userName.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        userName.setTranslateX(-110);
        userName.setFill(Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Your Name");
        usernameField.setFont(Font.font(FONT_FAMILY, 14));
        usernameField.setMaxWidth(300);
        usernameField.setFocusTraversable(false);
        usernameField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text gmail = new Text("Email");
        gmail.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        gmail.setTranslateX(-125);
        gmail.setFill(Color.WHITE);

        TextField gmailField = new TextField();
        gmailField.setPromptText("Enter your Email");
        gmailField.setFont(Font.font(FONT_FAMILY, 14));
        gmailField.setMaxWidth(300);
        gmailField.setFocusTraversable(false);
        gmailField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");

        Text userPassword = new Text("Password");
        userPassword.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        userPassword.setFill(Color.WHITE);
        userPassword.setTranslateX(-110);

        PasswordField passWordField = new PasswordField();
        passWordField.setPromptText("Enter Password");
        passWordField.setFont(Font.font(FONT_FAMILY, 14));
        passWordField.setMaxWidth(300);
        passWordField.setFocusTraversable(false);
        passWordField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(25, 27, 27, 0.29), 15, 0, 0, 5);");
        
        CheckBox termsCheckBox = new CheckBox("I agree to the");
        termsCheckBox.setFont(Font.font(FONT_FAMILY, 12));
        termsCheckBox.setTextFill(Color.WHITE);

        Hyperlink termsLink = new Hyperlink("Terms and Conditions");
        termsLink.setFont(Font.font(FONT_FAMILY, 16));
        termsLink.setOnAction(e -> {
            termsLink.setStyle(
            "-fx-text-fill: #8A2BE2;" + 
            "-fx-border-color: transparent;"
        );
            showTermsAndConditions();
        });

        HBox termsBox = new HBox(5, termsCheckBox, termsLink);
        termsBox.setAlignment(Pos.CENTER);

        Button signUpButton = new Button("Sign up");
        signUpButton.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        signUpButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);-fx-text-fill:white; -fx-background-radius : 30");
        signUpButton.setPrefHeight(40);
        signUpButton.setPrefWidth(120);
        signUpButton.setTranslateX(22);
        signUpButton.setFocusTraversable(false);
        signUpButton.setDisable(true);
        
        termsCheckBox.setOnAction(e -> signUpButton.setDisable(!termsCheckBox.isSelected()));

        Label resultLabel = new Label();
        resultLabel.setTextFill(Color.GREEN);
        resultLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 10));

        Button logInPageBtn = new Button("Back");
        logInPageBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        logInPageBtn.setMaxWidth(300);
        logInPageBtn.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);-fx-text-fill:white; -fx-background-radius : 30");
        logInPageBtn.setPrefHeight(40);
        logInPageBtn.setPrefWidth(120);
        logInPageBtn.setTranslateX(-22);

        logInPageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                back.run();
            }
        });

        HBox h1Box = new HBox(20, logInPageBtn, signUpButton);
        h1Box.setAlignment(Pos.CENTER);
        h1Box.setPadding(new Insets(10));

        VBox v1Box = new VBox(15, userName, usernameField, gmail, gmailField, userPassword, passWordField);
        v1Box.setAlignment(Pos.CENTER);
        v1Box.setPadding(new Insets(10));

        VBox formVBox = new VBox(25, createAccount, v1Box, termsBox, h1Box, resultLabel);
        formVBox.setAlignment(Pos.CENTER);
        formVBox.setPadding(new Insets(40));
        formVBox.setPrefHeight(600);
        formVBox.setMaxWidth(700);
        formVBox.setStyle(
            "-fx-background-color:linear-gradient(to bottom, #1b2735, #090a0f);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(18, 17, 17, 0.34), 15, 0, 0, 5);"
        );

        ProgressIndicator loader = new ProgressIndicator();
        loader.setVisible(false);

        signUpButton.setOnAction(e -> {
            username = usernameField.getText().trim();
            SignUpData signUpData = new SignUpData();
            if (signUpData.isUsernameTaken(username)) {
                showFancyAlert("Username Already Exists", "Please choose a different username.");
                return;
            }
            
            loader.setVisible(true);
            formVBox.setDisable(true);

            email = gmailField.getText().trim();
            String password = passWordField.getText().trim();

            Task<String> signUpTask = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    String result = SignUpControl.signInWithEmailAndPassword(email, password);
                    if (result.equals("success")) {
                        SignUpModel signUpModel = new SignUpModel(username, email, password);
                        Map<String, String> data = signUpModel.getMap();
                        signUpData.addSignUpData(data, email);
                    }
                    return result;
                }
            };

            signUpTask.setOnSucceeded(event -> {
                loader.setVisible(false);
                formVBox.setDisable(false);
                String result = signUpTask.getValue();

                if (result.equals("success")) {
                    EnterDetails enterDetails = new EnterDetails();
                    HBox editProfileLayout = enterDetails.getEditProfileLayout(p2Stage);
                    Scene editProfileScene = new Scene(editProfileLayout, 1550, 800);
                    p2Stage.setScene(editProfileScene);
                } else {
                    if (result.startsWith("❌ Password issues:")) {
                        showFancyAlert("Weak Password", result);
                    } else {
                        showFancyAlert("Signup Failed", result);
                    }
                }
            });
            
            signUpTask.setOnFailed(event -> {
                loader.setVisible(false);
                formVBox.setDisable(false);
                showFancyAlert("Error", "An unexpected error occurred during sign-up.");
            });

            new Thread(signUpTask).start();
        });

        VBox mainSignUpContainer = new VBox(formVBox);
        mainSignUpContainer.setStyle("-fx-background-color: black;");
        mainSignUpContainer.setAlignment(Pos.CENTER);
        
        StackPane root = new StackPane(mainSignUpContainer, loader);
        root.setAlignment(Pos.CENTER);
        
        return root;
    }

    private void showFancyAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(title);
        String baseTitleStyle = "-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;";
        if (title.toLowerCase().contains("already") || title.toLowerCase().contains("failed") || title.toLowerCase().contains("error")) {
            titleText.setStyle(baseTitleStyle + "-fx-fill: red;");
        } else {
            titleText.setStyle(baseTitleStyle + "-fx-fill: green;");
        }

        Text messageText = new Text(message);
        messageText.setStyle("-fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");
        messageText.setWrappingWidth(350);

        Button closeButton = new Button("OK");
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
        alertBox.setStyle("-fx-background-color: #2e2e2e;-fx-background-radius: 25; -fx-padding: 25;");
        alertBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(alertBox);

        scene.setFill(Color.TRANSPARENT);

        alertStage.setScene(scene);
        alertStage.show();
    }
    
    private void showTermsAndConditions() {
        Stage termsStage = new Stage();
        termsStage.initModality(Modality.APPLICATION_MODAL);
        termsStage.setTitle("Terms and Conditions");

        String termsText = 
            "Terms And Conditions\n\n" +
            "End User License Agreement\n\n" +
            "Smart Samadhan is licensed to You (End-User) by Smart Samadhan (hereinafter Licensor), for use only under the terms of this License Agreement. By downloading the Application from the Google Play Store, and any update thereto (as permitted by this License Agreement), You indicate that You agree to be bound by all of the terms and conditions of this License Agreement and that you accept this License Agreement. The parties of this License Agreement acknowledge that Google is not a Party to this License Agreement and is not bound by any provisions or obligations concerning the Application, such as warranty, liability, maintenance and support thereof. Smart Samadhan, not Google, is solely responsible for the licensed application and the content thereof. This License Agreement may not provide for usage rules for the Application that conflicts with the latest Play Store Terms of Service. Smart Samadhan acknowledges that it had the opportunity to review said terms and this License Agreement is not conflicting with them. All rights not expressly granted to You are reserved.\n\n" +
            "The Application\n\n" +
            "Smart Samadhan (hereinafter Application) is a piece of software created to serve as a social media platform for issue reporting and customized for Desktop devices. It is used to ask questions, report issues, and share experiences.\n\n" +
            "Scope Of License\n\n" +
            "You are given a non-transferable, non-exclusive, non-sublicensable license to install and use the Licensed Application on any Android branded products that You (End User) own or control and as permitted by the Usage Rules outlined in this section and the Play Store Terms of Service... \n\n" +
            "Contact Information\n\n" +
            "For general inquiries, complaints, questions or claims concerning the licensed Application, please contact:\n" +
            "Smart Samadhan\n" +
            "located at Core2Web, 3rd Floor, Walhekar Properties, Narhe Road, Near Navale Bridge\n" +
            "Narhe, Maharashtra, 411041\n" +
            "India\n" +
            "support@smartsamadhan.com\n\n" +
            "©2025 Smart Samadhan®";
        
        TextArea textArea = new TextArea(termsText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-control-inner-background: #2e2e2e;");
        textArea.setFont(Font.font(FONT_FAMILY, 14));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> termsStage.close());

        VBox layout = new VBox(10, textArea, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color:linear-gradient(to bottom, #1b2735, #090a0f);");

        Scene scene = new Scene(layout, 600, 400);
        termsStage.setScene(scene);
        termsStage.showAndWait();
    }
}