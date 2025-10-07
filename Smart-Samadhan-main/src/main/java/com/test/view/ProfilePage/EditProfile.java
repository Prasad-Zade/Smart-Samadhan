package com.test.view.ProfilePage;

import com.test.dao.EditProfileData;
import com.test.dao.UpdatedProfileData;
import com.test.model.UpdatedProfileModel;
import com.test.util.LanguageManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditProfile {
    private Scene myeditProfileScene;
    private Stage myeditProfileStage;
    private BorderPane bp;
   
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(EditProfile.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(EditProfile.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setMyeditProfileScene(Scene myeditProfileScene) {
        this.myeditProfileScene = myeditProfileScene;
    }

    public void setMyeditProfileStage(Stage myeditProfileStage) {
        this.myeditProfileStage = myeditProfileStage;
    }

    public VBox createEditProfileScene(Stage primaryStage, BorderPane bp) {
        this.bp = bp;
        this.myeditProfileStage = primaryStage;

        Image profile = new Image(EditProfileData.profileImg);
        ImageView imageView = new ImageView(profile);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setClip(new Circle(75, 75, 75));
        imageView.setTranslateY(-120);
        imageView.setSmooth(true);

        Text dataText = new Text(lang.getString("editProfile.personalInfo"));
        dataText.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';-fx-fill: #007bff");
        dataText.setTranslateY(-10);

        String labelStyle = "-fx-font-size: 14px;-fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white";
        String fieldStyle = "-fx-font-size: 14; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-radius: 8; -fx-control-inner-background: #2e2e2e;";

        Text firstname = new Text(lang.getString("editProfile.firstName"));
        firstname.setStyle(labelStyle);
        TextField firstNameTextField = new TextField();
        firstNameTextField.setText(EditProfileData.firstName);
        firstNameTextField.setStyle(fieldStyle);
        firstNameTextField.setMinWidth(300);

        Text lastname = new Text(lang.getString("editProfile.lastName"));
        lastname.setStyle(labelStyle);
        TextField lastNameTextField = new TextField();
        lastNameTextField.setText(EditProfileData.lastName);
        lastNameTextField.setStyle(fieldStyle);
        lastNameTextField.setMinWidth(300);

        Text userName = new Text(lang.getString("editProfile.username"));
        userName.setStyle(labelStyle);
        TextField userNameTextField = new TextField();
        userNameTextField.setText(EditProfileData.username);
        userNameTextField.setStyle(fieldStyle);
        userNameTextField.setMinWidth(300);

        Text gender = new Text(lang.getString("editProfile.gender"));
        gender.setStyle(labelStyle);
        TextField outputTextField = new TextField();
        outputTextField.setText(EditProfileData.genderdetails);
        outputTextField.setStyle(fieldStyle);
        outputTextField.setMinWidth(300);

        HBox genderHBox = new HBox(10, outputTextField);

        Text email = new Text(lang.getString("editProfile.email"));
        email.setStyle(labelStyle);
        TextField emailTextField = new TextField();
        emailTextField.setText(EditProfileData.Email);
        emailTextField.setStyle(fieldStyle);
        emailTextField.setMaxWidth(640);

        Text address = new Text(lang.getString("editProfile.address"));
        address.setStyle(labelStyle);
        TextField addressTextField = new TextField();
        addressTextField.setText(EditProfileData.Address);
        addressTextField.setStyle(fieldStyle);
        addressTextField.setMaxWidth(640);

        Text phoneNum = new Text(lang.getString("editProfile.phone"));
        phoneNum.setStyle(labelStyle);
        TextField phoneNumTextField = new TextField();
        phoneNumTextField.setText(EditProfileData.phoneNo);
        phoneNumTextField.setStyle(fieldStyle);
        phoneNumTextField.setMinWidth(300);

        Text DOB = new Text(lang.getString("editProfile.dob"));
        DOB.setStyle(labelStyle);
        TextField DOBTextField = new TextField();
        DOBTextField.setText(EditProfileData.Dob);
        DOBTextField.setStyle(fieldStyle);
        DOBTextField.setMinWidth(300);

        Text location = new Text(lang.getString("editProfile.location"));
        location.setStyle(labelStyle);
        TextField locationTextField = new TextField();
        locationTextField.setText(EditProfileData.Location);
        locationTextField.setStyle(fieldStyle);
        locationTextField.setMinWidth(300);

        Text postalCode = new Text(lang.getString("editProfile.postalCode"));
        postalCode.setStyle(labelStyle);
        TextField postalCodeTextField = new TextField();
        postalCodeTextField.setText(EditProfileData.PostalCode);
        postalCodeTextField.setStyle(fieldStyle);
        postalCodeTextField.setMinWidth(300);

        String buttonStyle = "-fx-background-radius: 15; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-background-color: linear-gradient(from 0% 0% to 100% 0%, #00c6ff, #0072ff)";

        Button save = new Button(lang.getString("editProfile.saveButton"));
        save.setTranslateX(-100);
        save.setTranslateY(20);
        save.setStyle(buttonStyle);
        save.setOnAction(event -> {
            // MODIFIED - All validation messages now use language keys
            if (firstNameTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.firstNameEmpty.message"); return;
            }
            if (lastNameTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.lastNameEmpty.message"); return;
            }
            if (outputTextField.getText() == null || outputTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.genderEmpty.message"); return;
            }
            if (addressTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.addressEmpty.message"); return;
            }
            if (phoneNumTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.phoneEmpty.message"); return;
            } else if (!phoneNumTextField.getText().trim().matches("\\d{10}")) {
                showFancyAlert("alert.validationError.title", "alert.phoneInvalid.message"); return;
            }
            if (DOBTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.dobEmpty.message"); return;
            } else if (!DOBTextField.getText().trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
                showFancyAlert("alert.validationError.title", "alert.dobInvalid.message"); return;
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dobDate = LocalDate.parse(DOBTextField.getText().trim(), formatter);
                    if (dobDate.isAfter(LocalDate.now().minusYears(12))) {
                        showFancyAlert("alert.ageRestriction.title", "alert.ageRestriction.message"); return;
                    }
                } catch (DateTimeParseException ex) {
                    showFancyAlert("alert.validationError.title", "alert.dateParseException.message"); return;
                }
            }
            if (locationTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.locationEmpty.message"); return;
            }
            if (postalCodeTextField.getText().trim().isEmpty()) {
                showFancyAlert("alert.validationError.title", "alert.postalCodeEmpty.message"); return;
            } else if (!postalCodeTextField.getText().trim().matches("\\d{6}")) {
                showFancyAlert("alert.validationError.title", "alert.postalCodeInvalid.message"); return;
            }

            UpdatedProfileModel updatedModel = new UpdatedProfileModel(
                firstNameTextField.getText(), lastNameTextField.getText(), userNameTextField.getText(), outputTextField.getText(),
                emailTextField.getText(), addressTextField.getText(), phoneNumTextField.getText(), DOBTextField.getText(),
                locationTextField.getText(), postalCodeTextField.getText()
            );

            UpdatedProfileData.updateUserData(emailTextField.getText(), updatedModel);
            System.out.println("Profile updated successfully.");
            bp.setCenter(new ProfilePage().createProfileUI(primaryStage, bp));
        });

        Button discard = new Button(lang.getString("editProfile.discardButton"));
        discard.setTranslateX(-30);
        discard.setTranslateY(20);
        discard.setStyle(buttonStyle);
        discard.setOnAction(arg0 -> bp.setCenter(new ProfilePage().createProfileUI(primaryStage, bp)));

        HBox buttonHBox = new HBox(20, save, discard);
        buttonHBox.setAlignment(Pos.CENTER);
        VBox image = new VBox(imageView);
        image.setTranslateX(65);
        image.setTranslateY(80);
        HBox name = new HBox(272, firstname, lastname);
        HBox nameTextField = new HBox(40, firstNameTextField, lastNameTextField);
        HBox username_gender = new HBox(272, userName, gender);
        HBox user_gender = new HBox(40, userNameTextField, genderHBox);
        HBox phoneDOB = new HBox(245, phoneNum, DOB);
        HBox phoneDOBTextField = new HBox(40, phoneNumTextField, DOBTextField);
        HBox locPos = new HBox(290, location, postalCode);
        HBox locPosTextField = new HBox(40, locationTextField, postalCodeTextField);
        VBox ProfileInfo = new VBox(11, dataText, name, nameTextField, username_gender, user_gender);
        HBox imgInfo = new HBox(ProfileInfo, image);
        VBox detailsbar = new VBox(11, imgInfo, email, emailTextField, address, addressTextField, phoneDOB, phoneDOBTextField, locPos, locPosTextField, buttonHBox);
        detailsbar.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-padding: 80 100 80 100; -fx-background-radius: 35; ");
        detailsbar.setMaxWidth(900);
        detailsbar.setMaxHeight(400);

        VBox editProfileVBox = new VBox(detailsbar);
        editProfileVBox.setStyle("-fx-background-color: black");
        editProfileVBox.setMinHeight(400);
        editProfileVBox.setAlignment(Pos.CENTER);

        return editProfileVBox;
    }

    private void showFancyAlert(String titleKey, String messageKey) {
        Stage alertStage = new Stage();
        alertStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        alertStage.setAlwaysOnTop(true);

        Text titleText = new Text(lang.getString(titleKey));
        titleText.setStyle("-fx-font-size: 18px; -fx-font-family: '" + FONT_FAMILY + "'; -fx-fill: white; -fx-font-weight: bold;");

        Text messageText = new Text(lang.getString(messageKey));
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
        alertBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f);-fx-background-radius: 25; -fx-padding: 25;");
        alertBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(alertBox);
        scene.setFill(Color.TRANSPARENT);

        alertStage.setScene(scene);
        alertStage.setWidth(350);
        alertStage.setHeight(200);
        alertStage.show();
    }
}