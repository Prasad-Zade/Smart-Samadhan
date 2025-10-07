package com.test.view.Home;

import com.test.util.LanguageManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class About {

    private Stage primaryStage;
    private Scene aboutScene, homeScene;
    private final LanguageManager lang = LanguageManager.getInstance();

    private static final String FONT_FAMILY = "Poppins";

    static {
        try {
            Font.loadFont(About.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(About.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAboutScene(Scene aboutScene) {
        this.aboutScene = aboutScene;
    }

    public Parent createAboutScene(Runnable back) {
        // --- Reusable Effects ---
        DropShadow imageGlow = new DropShadow(30, Color.rgb(0, 123, 255, 0.7));
        imageGlow.setSpread(0.25);
        DropShadow labelGlow = new DropShadow(20, Color.rgb(0, 123, 255, 0.9));
        labelGlow.setSpread(0.3);
        DropShadow profileHoverGlow = new DropShadow(25, Color.rgb(255, 255, 255, 0.5));
        profileHoverGlow.setSpread(0.3);

        // --- Title ---
        Label aboutLabel = new Label(lang.getString("about.title"));
        aboutLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #007bff; -fx-font-family: '" + FONT_FAMILY + "';");
        addHoverEffect(aboutLabel, labelGlow);

        // --- Logo and About Text Section ---
        // Create an ImageView for your logo
        ImageView logoView = new ImageView(new Image("assets\\Images\\Untitled_design__7_-removebg-preview.png")); // <-- IMPORTANT: REPLACE WITH YOUR LOGO PATH
        logoView.setFitHeight(400); // Adjust size as needed
        logoView.setPreserveRatio(true);
        logoView.setEffect(imageGlow);

        // Create a label to go below the logo
        Label byCore2WebLabel = new Label("By Core2Web");
        byCore2WebLabel.setStyle("-fx-font-size: 30; -fx-text-fill: yellow; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-style: italic;");
        
        // VBox to hold the logo and the "By Core2Web" text
        VBox logoContainer = new VBox(logoView, byCore2WebLabel);
        logoContainer.setAlignment(Pos.CENTER);


        // Create the about text with a smaller wrapping width to accommodate the logo
        Text aboutText = createStyledText(lang.getString("about.introText"), 550); // Reduced wrapping width
        aboutText.setTextAlignment(TextAlignment.JUSTIFY);

        // Create an HBox to hold the logo container and intro text
        HBox introBox = new HBox(80,aboutText,logoContainer); // Use logoContainer instead of logoView
        introBox.setAlignment(Pos.CENTER_LEFT);


        // --- Core2Web Section ---
        VBox core2webSection = createTitledSection(
            lang.getString("about.core2web.title"),
            createAnimatedImageView("assets/Images/Il1s7VYRV23p_J7m1rS8y96ldviGz0aC.png", imageGlow),
            createStyledText(lang.getString("about.core2web.description"), 700),
            labelGlow
        );

        // --- Mentor Section ---
        VBox mentorSection = createTitledSection(
            lang.getString("about.mentor.title"),
            createAnimatedImageView("assets\\Images\\gurudev-removebg-preview.png", imageGlow),
            createStyledText(lang.getString("about.mentor.description"), 700),
            labelGlow
        );
        HBox mentorInfo = (HBox) mentorSection.getChildren().get(1);
        mentorInfo.getChildren().setAll(mentorInfo.getChildren().get(1), mentorInfo.getChildren().get(0)); // Swaps image and text

        // --- Special Thanks Section ---
        Label specialThanksLabel = new Label(lang.getString("about.instructors.title"));
        specialThanksLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #007bff; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;");
        addHoverEffect(specialThanksLabel, labelGlow);

        VBox sachinSirProfile = createProfileVBox(lang.getString("about.instructors.sachin.name"), lang.getString("about.instructors.sachin.description"));
        VBox pramodSirProfile = createProfileVBox(lang.getString("about.instructors.pramod.name"), lang.getString("about.instructors.pramod.description"));
        VBox akshaySirProfile = createProfileVBox(lang.getString("about.instructors.akshay.name"), lang.getString("about.instructors.akshay.description"));

        addProfileHoverEffect(sachinSirProfile, profileHoverGlow);
        addProfileHoverEffect(pramodSirProfile, profileHoverGlow);
        addProfileHoverEffect(akshaySirProfile, profileHoverGlow);

        FlowPane thanksFlowPane = new FlowPane(50, 30);
        thanksFlowPane.setAlignment(Pos.CENTER);
        thanksFlowPane.getChildren().addAll(sachinSirProfile, pramodSirProfile, akshaySirProfile);

        Label additionalThanksLabel = new Label(lang.getString("about.additionalThanks.title"));
        additionalThanksLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #007bff; -fx-font-family: '" + FONT_FAMILY + "';");
        addHoverEffect(additionalThanksLabel, labelGlow);

        Text namesText = new Text(lang.getString("about.additionalThanks.names"));
        namesText.setStyle("-fx-font-size: 16; -fx-fill: #cccccc; -fx-font-family: '" + FONT_FAMILY + "';");
        namesText.setTextAlignment(TextAlignment.CENTER);
        
        VBox additionalThanksBox = new VBox(10, additionalThanksLabel, namesText);
        additionalThanksBox.setAlignment(Pos.CENTER_LEFT);

        VBox specialThanksSection = new VBox(30, specialThanksLabel, thanksFlowPane, additionalThanksBox);
        specialThanksSection.setAlignment(Pos.CENTER_LEFT);

        // --- Our Team Section ---
        Label teamLabel = new Label(lang.getString("about.team.title"));
        teamLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #007bff; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;");
        addHoverEffect(teamLabel, labelGlow);

        VBox member1 = createTeamMemberVBox("assets/Images/self.jpg", lang.getString("about.team.nadeem.name"));
        VBox member2 = createTeamMemberVBox("assets\\Images\\prasadzade.jpg", lang.getString("about.team.prasad.name"));
        VBox member3 = createTeamMemberVBox("assets\\Images\\piyush.jpg", lang.getString("about.team.piyush.name"));
        VBox member4 = createTeamMemberVBox("assets\\Images\\gaurav.jpg", lang.getString("about.team.gaurav.name"));

        addProfileHoverEffect(member1, profileHoverGlow);
        addProfileHoverEffect(member2, profileHoverGlow);
        addProfileHoverEffect(member3, profileHoverGlow);
        addProfileHoverEffect(member4, profileHoverGlow);

        FlowPane teamFlowPane = new FlowPane(50, 30);
        teamFlowPane.setAlignment(Pos.CENTER);
        teamFlowPane.getChildren().addAll(member1, member2, member3, member4);

        VBox teamSection = new VBox(20, teamLabel, teamFlowPane);
        teamSection.setAlignment(Pos.CENTER_LEFT);

        // --- Main VBox Layout ---
        VBox aboutVBox = new VBox(40, aboutLabel, introBox, core2webSection, mentorSection, specialThanksSection, teamSection);
        aboutVBox.setPadding(new Insets(40));
        aboutVBox.setAlignment(Pos.TOP_LEFT);
        aboutVBox.setStyle("-fx-background-color: transparent;");

        // --- ScrollPane and Final Container ---
        ScrollPane scrollPane = new ScrollPane(aboutVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox(scrollPane);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        return container;
    }
    
    // --- HELPER METHODS ---
    private void addHoverEffect(Label label, DropShadow effect) {
        label.setOnMouseEntered(e -> {
            label.setEffect(effect);
            label.setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(e -> {
            label.setEffect(null);
            label.setCursor(Cursor.DEFAULT);
        });
    }

    private void addProfileHoverEffect(VBox profileBox, DropShadow hoverEffect) {
        profileBox.setOnMouseEntered(e -> {
            profileBox.setEffect(hoverEffect);
            profileBox.setScaleX(1.05);
            profileBox.setScaleY(1.05);
            profileBox.setCursor(Cursor.HAND);
        });
        profileBox.setOnMouseExited(e -> {
            profileBox.setEffect(null);
            profileBox.setScaleX(1.0);
            profileBox.setScaleY(1.0);
            profileBox.setCursor(Cursor.DEFAULT);
        });
    }

    private Text createStyledText(String content, double wrappingWidth) {
        Text text = new Text(content);
        text.setStyle("-fx-font-size: 16; -fx-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");
        text.setWrappingWidth(wrappingWidth);
        return text;
    }

    private ImageView createAnimatedImageView(String imagePath, DropShadow effect) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setEffect(effect);

        ScaleTransition st = new ScaleTransition(Duration.seconds(2), imageView);
        st.setByX(0.05);
        st.setByY(0.05);
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.setAutoReverse(true);
        st.play();

        return imageView;
    }
    
    private VBox createTitledSection(String title, ImageView imageView, Text text, DropShadow labelGlow) {
        Label label = new Label(title);
        label.setStyle("-fx-font-size: 20; -fx-text-fill: #007bff; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: bold;");
        addHoverEffect(label, labelGlow);

        HBox infoBox = new HBox(80, imageView, text);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(10, 0, 0, 0));

        VBox section = new VBox(15, label, infoBox);
        section.setAlignment(Pos.CENTER_LEFT);
        return section;
    }

    private VBox createProfileVBox(String name, String description) {

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

        Text descText = createStyledText(description, 220);
        descText.setTextAlignment(TextAlignment.CENTER);

        VBox profileBox = new VBox(10,nameLabel, descText);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPrefWidth(250);
        
        return profileBox;
    }
    
    private VBox createTeamMemberVBox(String imagePath, String name) {
        ImageView profileView = new ImageView(new Image(imagePath));
        profileView.setFitWidth(120);
        profileView.setFitHeight(120);
        
        Circle clip = new Circle(60, 60, 60);
        profileView.setClip(clip);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "';");

        VBox profileBox = new VBox(10, profileView, nameLabel);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPrefWidth(200);
        
        return profileBox;
    }
}