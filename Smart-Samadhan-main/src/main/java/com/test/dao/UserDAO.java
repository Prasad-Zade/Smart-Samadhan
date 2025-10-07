package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.test.servies.InitializeFirbase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserDAO {
    
    private static final Firestore db = InitializeFirbase.getdb();
    private static final String COLLECTION_NAME = "users";

    /**
     * Get user display name by email/userId
     * @param userId The user's email or ID
     * @return The user's display name, or the userId if no display name is found
     */
    public static String getUserDisplayName(String userId) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", userId)
                    .limit(1)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                String displayName = document.getString("displayName");
                if (displayName != null && !displayName.trim().isEmpty()) {
                    return displayName;
                }
                
                // Fallback to first name + last name
                String firstName = document.getString("firstName");
                String lastName = document.getString("lastName");
                if (firstName != null && lastName != null) {
                    return firstName + " " + lastName;
                }
                
                // Fallback to just first name
                if (firstName != null) {
                    return firstName;
                }
            }
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching user display name: " + e.getMessage());
        }
        
        // Return the userId (email) if no display name is found
        // You could also extract just the part before @ for a cleaner display
        if (userId.contains("@")) {
            return userId.substring(0, userId.indexOf("@"));
        }
        return userId;
    }

    /**
     * Update user display name
     * @param userId The user's email or ID
     * @param displayName The new display name
     */
    public static void updateUserDisplayName(String userId, String displayName) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", userId)
                    .limit(1)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                document.getReference().update("displayName", displayName);
                System.out.println("[UserDAO] Updated display name for " + userId);
            } else {
                // Create a new user document if it doesn't exist
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", userId);
                userData.put("displayName", displayName);
                userData.put("createdAt", System.currentTimeMillis());
                
                db.collection(COLLECTION_NAME).add(userData);
                System.out.println("[UserDAO] Created new user with display name: " + displayName);
            }
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating user display name: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if a user exists in the database
     * @param userId The user's email or ID
     * @return true if user exists, false otherwise
     */
    public static boolean userExists(String userId) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", userId)
                    .limit(1)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            return !querySnapshot.getDocuments().isEmpty();
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create or update user profile
     * @param userId The user's email or ID
     * @param firstName User's first name
     * @param lastName User's last name
     * @param displayName User's display name
     */
    public static void createOrUpdateUser(String userId, String firstName, String lastName, String displayName) {
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", userId);
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);
            userData.put("displayName", displayName);
            userData.put("lastUpdated", System.currentTimeMillis());

            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", userId)
                    .limit(1)
                    .get();

            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.getDocuments().isEmpty()) {
                // Update existing user
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                document.getReference().update(userData);
                System.out.println("[UserDAO] Updated user: " + userId);
            } else {
                // Create new user
                userData.put("createdAt", System.currentTimeMillis());
                db.collection(COLLECTION_NAME).add(userData);
                System.out.println("[UserDAO] Created new user: " + userId);
            }
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error creating/updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}