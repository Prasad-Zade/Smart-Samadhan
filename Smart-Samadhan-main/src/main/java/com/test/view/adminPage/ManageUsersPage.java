package com.test.view.adminPage;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.test.model.User;
import com.test.servies.FirebaseAuthService;
import com.test.servies.InitializeFirbase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ManageUsersPage {

    private static final String FONT_FAMILY = "Poppins";
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private VBox usersContainer;
    private TextField searchField;
    private Label statusLabel;

    static {
        try {
            Font.loadFont(ManageUsersPage.class.getResourceAsStream("/assets/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(ManageUsersPage.class.getResourceAsStream("/assets/fonts/Poppins-Bold.ttf"), 10);
        } catch (Exception e) {
            System.err.println("Could not load Poppins font. Using default system font.");
        }
    }

    public VBox createUserManagementScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2735, #090a0f); -fx-background-radius: 25;");

        Text heading = new Text("Manage Users");
        heading.setStyle("-fx-font-size: 28; -fx-fill: white; -fx-font-weight: bold; -fx-font-family: '" + FONT_FAMILY + "';");

        // Search bar setup
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Enter username to search");
        searchField.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        // This will stretch search field to take max width
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 10;");
        searchButton.setOnAction(e -> searchUser());

        searchBox.getChildren().addAll(searchField, searchButton);
        searchBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: white;");

        // User list container inside ScrollPane
        usersContainer = new VBox(10);
        usersContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(usersContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        // Make scrollPane background match parent and hide borders
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-padding: 0;" +
            "-fx-control-inner-background: transparent;"
        );

        root.getChildren().addAll(heading, searchBox, statusLabel, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // ScrollPane expands if space allows

        loadUserData();
        return root;
    }

    private void loadUserData() {
        Task<ObservableList<User>> task = new Task<>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                ObservableList<User> list = FXCollections.observableArrayList();
                for (DocumentSnapshot doc : InitializeFirbase.getCollection("users").get().get().getDocuments()) {
                    String userId = doc.getId();
                    String name = doc.contains("userName") ? doc.getString("userName") : "";
                    String email = doc.contains("userEmail") ? doc.getString("userEmail") : "";
                    String role = doc.contains("role") ? doc.getString("role") : "";

                    list.add(new User(userId, name, email, role));
                }
                return list;
            }
        };

        task.setOnSucceeded(e -> {
            userList.clear();
            userList.addAll(task.getValue());
            showUsers(userList);
        });

        task.setOnFailed(e -> {
            System.err.println("Failed to fetch users");
            if (task.getException() != null) task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void showUsers(ObservableList<User> users) {
        usersContainer.getChildren().clear();

        if (users.isEmpty()) {
            Label noUserLabel = new Label("No users found.");
            noUserLabel.setStyle("-fx-text-fill: white;");
            usersContainer.getChildren().add(noUserLabel);
            return;
        }

        for (User user : users) {
            HBox userBox = new HBox(20);
            userBox.setPadding(new Insets(10));
            userBox.setStyle("-fx-background-color: #2e3b4e; -fx-background-radius: 10;");
            userBox.setPrefHeight(70);

            VBox nameEmailBox = new VBox(5);
            Label nameLabel = new Label(user.getName() != null ? user.getName() : "Unknown");
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-family: '" + FONT_FAMILY + "';");

            Label emailLabel = new Label(user.getEmail() != null ? user.getEmail() : "No Email");
            emailLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13; -fx-font-family: '" + FONT_FAMILY + "';");

            nameEmailBox.getChildren().addAll(nameLabel, emailLabel);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button deleteButton = new Button("Remove");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 10;");
            deleteButton.setOnAction(e -> deleteUser(user));

            userBox.getChildren().addAll(nameEmailBox, spacer, deleteButton);
            usersContainer.getChildren().add(userBox);
        }
    }

    private void searchUser() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            statusLabel.setText("Showing all users.");
            showUsers(userList);
            return;
        }

        ObservableList<User> filtered = FXCollections.observableArrayList();

        for (User user : userList) {
            if (user.getName() != null && user.getName().toLowerCase().contains(searchText)) {
                filtered.add(user);
            }
        }

        if (filtered.isEmpty()) {
            statusLabel.setText("User not found.");
        } else {
            statusLabel.setText("Found " + filtered.size() + " user(s).");
        }

        showUsers(filtered);
    }

    private void deleteUser(User user) {
        Task<Void> deleteTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Delete from Firestore
                InitializeFirbase.getCollection("users")
                    .document(user.getUserId()) // Firestore ID
                    .delete()
                    .get();

                // Delete from Firebase Authentication using email
                boolean authDeleted = FirebaseAuthService.deleteUserByEmail(user.getEmail());

                if (!authDeleted) {
                    System.err.println("⚠ Could not delete from Firebase Auth: " + user.getEmail());
                }

                return null;
            }
        };

        deleteTask.setOnSucceeded(e -> {
            userList.remove(user);
            showUsers(userList);
            statusLabel.setText("✅ User removed: " + user.getName());
        });

        deleteTask.setOnFailed(e -> {
            System.err.println("❌ Failed to delete user: " + user.getEmail());
            if (deleteTask.getException() != null) deleteTask.getException().printStackTrace();
            statusLabel.setText("❌ Failed to delete user.");
        });

        new Thread(deleteTask).start();
    }

}