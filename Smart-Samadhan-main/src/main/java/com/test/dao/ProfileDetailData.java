// package com.test.dao;

// import com.google.cloud.firestore.DocumentSnapshot;
// import com.test.servies.InitializeFirbase;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;

// public class ProfileDetailData {

    
//     private static String loggedInUserEmail;
//     private static String location;
//     public static String firstName;
//     private static String lastName;
//     private static String userName;
//     private static String imgProfile;
//     public static String phoneNumber;
    
//     private static List<String> userPosts = new ArrayList<>();


   
//     public static void loadUserDetails(String email) {
//         if (email == null || email.isEmpty()) {
//             System.err.println("❌ Cannot load user details: email is null or empty.");
//             return;
//         }

        
//         loggedInUserEmail = email;
//         System.out.println("🔄 Loading details for user: " + loggedInUserEmail);

//         try {
            
//             DocumentSnapshot response = InitializeFirbase.db.collection("users").document(loggedInUserEmail).get().get();

//             if (response.exists()) {
//                 Map<String, Object> responseData = response.getData();

                
//                 firstName = (String) responseData.get("firstName");
//                 lastName = (String) responseData.get("lastName");
//                 userName = (String) responseData.get("userName");
//                 location = (String) responseData.get("location");
//                 imgProfile = (String) responseData.get("imageUrl");
//                 phoneNumber = (String) responseData.get("phoneNo");

                
//                 if (responseData.containsKey("userPosts") && responseData.get("userPosts") instanceof List) {
                
//                     userPosts = (List<String>) responseData.get("userPosts");
//                 } else {
//                     userPosts = new ArrayList<>(); 
//                 }

//                 System.out.println("✅ User details loaded successfully for " + userName);
//             } else {
//                 System.err.println("❌ User document not found for email: " + loggedInUserEmail);
                
//                 clearUserDetails();
//             }

//         } catch (Exception e) {
//             System.err.println("❌ Failed to fetch user data for " + loggedInUserEmail);
//             e.printStackTrace();
//             clearUserDetails();
//         }
//     }

//     private static List<String> savedPosts = new ArrayList<>();

//     // Call this method after fetching user details on login
//     public static void setSavedPosts(List<String> savedPosts) {
//         if (savedPosts != null) {
//             ProfileDetailData.savedPosts = savedPosts;
//         } else {
//             ProfileDetailData.savedPosts.clear();
//         }
//     }

//     public static List<String> getSavedPosts() {
//         return savedPosts;
//     }

//     public static void clearUserDetails() {
//         loggedInUserEmail = null;
//         location = null;
//         firstName = null;
//         lastName = null;
//         userName = null;
//         imgProfile = null;
//         userPosts = new ArrayList<>();
//         System.out.println("🗑️ User details cleared.");
//     }

//     public static String getUID() {
//         return loggedInUserEmail;
//     }

//     public static String getLocation() {
//         return location;
//     }

//     public static String getFirstName() {
//         return firstName;
//     }

//     public static String getLastName() {
//         return lastName;
//     }

//     public static String getUserName() {
//         return userName;
//     }

//     public static String getImgProfile() {
//         return imgProfile;
//     }

//     public static List<String> getUserPosts() {
//         return userPosts;
//     }

//     public static String getphoneNumber() {
//         return phoneNumber;
//     }
// }

package com.test.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.test.servies.InitializeFirbase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileDetailData {

    private static String loggedInUserEmail;
    private static String location;
    public static String firstName;
    private static String lastName;
    private static String userName;
    private static String imgProfile;
    public static String phoneNumber;
    private static List<String> userPosts = new ArrayList<>();
    private static List<String> savedPosts = new ArrayList<>();


    public static void loadUserDetails(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("❌ Cannot load user details: email is null or empty.");
            return;
        }

        loggedInUserEmail = email;
        System.out.println("🔄 Loading details for user: " + loggedInUserEmail);

        try {
            DocumentSnapshot response = InitializeFirbase.db.collection("users").document(loggedInUserEmail).get().get();

            if (response.exists()) {
                Map<String, Object> responseData = response.getData();

                firstName = (String) responseData.get("firstName");
                lastName = (String) responseData.get("lastName");
                userName = (String) responseData.get("userName");
                location = (String) responseData.get("location");
                imgProfile = (String) responseData.get("imageUrl");
                phoneNumber = (String) responseData.get("phoneNo");

                if (responseData.containsKey("userPosts") && responseData.get("userPosts") instanceof List) {
                    userPosts = (List<String>) responseData.get("userPosts");
                } else {
                    userPosts = new ArrayList<>();
                }
                
                // Also load savedPosts during the initial load and refresh
                if (responseData.containsKey("savedPosts") && responseData.get("savedPosts") instanceof List) {
                    savedPosts = (List<String>) responseData.get("savedPosts");
                } else {
                    savedPosts = new ArrayList<>();
                }

                System.out.println("✅ User details loaded successfully for " + userName);
            } else {
                System.err.println("❌ User document not found for email: " + loggedInUserEmail);
                clearUserDetails();
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to fetch user data for " + loggedInUserEmail);
            e.printStackTrace();
            clearUserDetails();
        }
    }

    /**
     * Re-fetches the details for the currently logged-in user from Firestore.
     * This method ensures that any cached data (like userPosts) is updated
     * with the latest information from the database.
     */
    public static void refreshUserDetails() {
        if (loggedInUserEmail == null || loggedInUserEmail.isEmpty()) {
            System.err.println("❌ Cannot refresh user details: no user is logged in.");
            return;
        }
        System.out.println("🔄 Refreshing user details...");
        // Calling loadUserDetails will re-run the fetch logic with the stored email
        loadUserDetails(loggedInUserEmail);
    }


    // Call this method after fetching user details on login
    public static void setSavedPosts(List<String> savedPosts) {
        if (savedPosts != null) {
            ProfileDetailData.savedPosts = savedPosts;
        } else {
            ProfileDetailData.savedPosts.clear();
        }
    }

    public static List<String> getSavedPosts() {
        return savedPosts;
    }

    public static void clearUserDetails() {
        loggedInUserEmail = null;
        location = null;
        firstName = null;
        lastName = null;
        userName = null;
        imgProfile = null;
        userPosts = new ArrayList<>();
        savedPosts = new ArrayList<>();
        System.out.println("🗑️ User details cleared.");
    }

    public static String getUID() {
        return loggedInUserEmail;
    }

    public static String getLocation() {
        return location;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getImgProfile() {
        return imgProfile;
    }

    public static List<String> getUserPosts() {
        return userPosts;
    }

    public static String getphoneNumber() {
        return phoneNumber;
    }
}