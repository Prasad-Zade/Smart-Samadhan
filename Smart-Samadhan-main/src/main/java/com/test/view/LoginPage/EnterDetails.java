package com.test.view.LoginPage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.test.dao.EnterDetailData;
import com.test.model.EnterDetailsModel;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EnterDetails {
    public String genderdetails;
    private String username;
    private String userEmail;
    public File selectedImageFile;

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(EnterDetails.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(EnterDetails.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public EnterDetails() {}

    public EnterDetails(String userName, String userEmail) {
        this.username = userName;
        this.userEmail = userEmail;
    }

    public HBox getEditProfileLayout(Stage stage) {
        Image profile = new Image("assets\\Images\\WhatsApp Image 2025-07-18 at 18.05.23_f29502e4.jpg");
        ImageView imageView = new ImageView(profile);
        imageView.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an Image");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                selectedImageFile = selectedFile;
                Image selectedImage = new Image(selectedFile.toURI().toString());
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
                imageView.setImage(selectedImage);
            }
        });

        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setClip(new Circle(75, 75, 75));

        Text userName = new Text(SignUp.username);
        userName.setStyle("-fx-font-family: '" + FONT_FAMILY + "';" +
                          "-fx-font-size: 26px;" +
                          "-fx-fill: linear-gradient(to right, #00c6ff, #0072ff);" +
                          "-fx-effect: dropshadow(one-pass-box, #00000088, 2, 0.0, 0, 1);");

        ImageView logo = new ImageView(new Image("assets\\Images\\Untitled_design__7_-removebg-preview.png"));
        logo.setFitHeight(300);
        logo.setFitWidth(300);
        logo.setTranslateY(30);

        VBox sideBar = new VBox(20, imageView, userName, logo);
        sideBar.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 5 50 100 50; -fx-background-radius: 35;");
        sideBar.setMaxHeight(693);
        sideBar.setTranslateX(130);
        sideBar.setTranslateY(40);
        sideBar.setAlignment(Pos.CENTER);

        Text dataText = new Text("Personal Information");
        dataText.setStyle("-fx-font-family: '" + FONT_FAMILY + "';" +
                          "-fx-font-size: 26px;" +
                          "-fx-fill: linear-gradient(to right, #00c6ff, #0072ff);" +
                          "-fx-effect: dropshadow(one-pass-box, #00000088, 2, 0.0, 0, 1);");
        dataText.setTranslateY(-10);

        Text firstname = new Text("First Name");
        firstname.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField firstNameTextField = new TextField();
        firstNameTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        firstNameTextField.setMinWidth(300);

        Text lastname = new Text("Last Name");
        lastname.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField lastNameTextField = new TextField();
        lastNameTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        lastNameTextField.setMinWidth(300);

        Text userLabel = new Text("UserName");
        userLabel.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField userNameTextField = new TextField();
        userNameTextField.setText(SignUp.username);
        userNameTextField.setEditable(false);
        userNameTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        userNameTextField.setMinWidth(300);

        Text gender = new Text("Gender :");
        gender.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setStyle("-fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setStyle("-fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

        ToggleGroup group = new ToggleGroup();
        maleButton.setToggleGroup(group);
        femaleButton.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == maleButton) {
                genderdetails = "Male";
            } else if (newToggle == femaleButton) {
                genderdetails = "Female";
            } else {
                genderdetails = null;
            }
        });

        HBox genderHBox = new HBox(10, gender, maleButton, femaleButton);

        Text email = new Text("Email");
        email.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField emailTextField = new TextField();
        emailTextField.setText(SignUp.email);
        emailTextField.setEditable(false);
        emailTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        emailTextField.setMaxWidth(700);

        Text address = new Text("Address");
        address.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField addressTextField = new TextField();
        addressTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        addressTextField.setMaxWidth(700);

        Text phoneNum = new Text("Phone Number");
        phoneNum.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField phoneNumTextField = new TextField();
        phoneNumTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        phoneNumTextField.setMinWidth(300);

        Text DOB = new Text("Date of Birth (DD/MM/YYYY)");
        DOB.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField DOBTextField = new TextField();
        DOBTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        DOBTextField.setMinWidth(300);

        Text location = new Text("Location");
        location.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField locationTextField = new TextField();
        locationTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        locationTextField.setMinWidth(300);

        Text postalCode = new Text("Postal Code");
        postalCode.setStyle("-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white");
        TextField postalCodeTextField = new TextField();
        postalCodeTextField.setStyle("-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;");
        postalCodeTextField.setMinWidth(300);

        Button next = new Button("Next");
        next.setTranslateX(295);
        next.setTranslateY(30);
        next.setMinHeight(40);
        next.setMinWidth(80);
        next.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: '" + FONT_FAMILY + "';" +
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff);"
        );

        next.setOnAction((ActionEvent e) -> {
            String firstName = firstNameTextField.getText().trim();
            String lastName = lastNameTextField.getText().trim();
            String username = userNameTextField.getText().trim();
            String Gender = genderdetails;
            String Email = emailTextField.getText().trim();
            String Address = addressTextField.getText().trim();
            String phoneNo = phoneNumTextField.getText().trim();
            String Dob = DOBTextField.getText().trim();
            String Location = locationTextField.getText().trim();
            String PostalCode = postalCodeTextField.getText().trim();

            if (firstName.isEmpty()) {
                showFancyAlert("Validation Error", "First name cannot be empty!");
                return;
            }
            if (selectedImageFile == null) {
                showFancyAlert("Validation Error", "Please upload a profile picture.");
                return;
            }
            if (lastName.isEmpty()) {
                showFancyAlert("Validation Error", "Last name cannot be empty!");
                return;
            }
            if (Gender == null || Gender.isEmpty()) {
                showFancyAlert("Validation Error", "Please select your gender.");
                return;
            }
            if (Address.isEmpty()) {
                showFancyAlert("Validation Error", "Address cannot be empty!");
                return;
            }
            if (phoneNo.isEmpty()) {
                showFancyAlert("Validation Error", "Phone number cannot be empty!");
                return;
            } else if (!phoneNo.matches("\\d{10}")) {
                showFancyAlert("Validation Error", "Please enter a valid 10-digit phone number.");
                return;
            }
            if (Dob.isEmpty()) {
                showFancyAlert("Validation Error", "Date of Birth cannot be empty!");
                return;
            } else if (!Dob.matches("\\d{2}/\\d{2}/\\d{4}")) {
                showFancyAlert("Validation Error", "Please enter Date of Birth in format DD/MM/YYYY.");
                return;
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dobDate = LocalDate.parse(Dob, formatter);
                    LocalDate today = LocalDate.now();
                    LocalDate minAllowedDob = today.minusYears(12);
                    if (dobDate.isAfter(minAllowedDob)) {
                        showFancyAlert("Age Restriction", "You must be at least 12 years old.");
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    showFancyAlert("Validation Error", "Invalid date. Please check the format and try again.");
                    return;
                }
            }
            if (Location.isEmpty()) {
                showFancyAlert("Validation Error", "Location cannot be empty!");
                return;
            }
            if (PostalCode.isEmpty()) {
                showFancyAlert("Validation Error", "Postal code cannot be empty!");
                return;
            } else if (!PostalCode.matches("\\d{6}")) {
                showFancyAlert("Validation Error", "Please enter a valid 6-digit postal code.");
                return;
            }

            EnterDetailsModel enterDetailsModel = new EnterDetailsModel(firstName, lastName, username, Gender, Email, Address, phoneNo, Dob, Location, PostalCode, selectedImageFile);
            Map<String, Object> data = enterDetailsModel.getMap();
            EnterDetailData enterDetailData = new EnterDetailData();
            enterDetailData.addEnterDetailsData(data, Email);
            if (selectedImageFile != null) {
                enterDetailData.updateProfile(selectedImageFile, Email);
            }

            Login loginPage = new Login();
            loginPage.setStage(stage);
            Scene loginScene = loginPage.createScene(stage);
            stage.setScene(loginScene);
            showFancyAlert("Success!", "Profile created successfully! Please log in.");
        });

        HBox name = new HBox(270, firstname, lastname);
        HBox nameTextField = new HBox(40, firstNameTextField, lastNameTextField);
        HBox phoneDOB = new HBox(245, phoneNum, DOB);
        HBox phoneDOBTextField = new HBox(40, phoneNumTextField, DOBTextField);
        HBox locPos = new HBox(290, location, postalCode);
        HBox locPosTextField = new HBox(40, locationTextField, postalCodeTextField);
        HBox usernameGender = new HBox(40, userNameTextField, genderHBox);

        VBox detailsbar = new VBox(13, dataText, name, nameTextField, userLabel, usernameGender,
            email, emailTextField, address, addressTextField, phoneDOB, phoneDOBTextField,
            locPos, locPosTextField, next);
        detailsbar.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 65 100 20 100; -fx-background-radius: 35;");
        detailsbar.setTranslateX(200);
        detailsbar.setTranslateY(40);
        detailsbar.setMaxWidth(900);
        detailsbar.setMaxHeight(700);

        HBox main = new HBox(20, sideBar, detailsbar);
        main.setStyle("-fx-background-color: black");

        return main;
    }

    private void showFancyAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white; -fx-font-weight: bold;");

        Text messageText = new Text(message);
        messageText.setStyle("-fx-font-size: 14px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white;");

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
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        alertStage.setScene(scene);
        alertStage.setWidth(350);
        alertStage.setHeight(200);
        alertStage.show();
    }
}