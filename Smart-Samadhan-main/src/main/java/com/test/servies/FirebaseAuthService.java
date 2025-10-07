package com.test.servies;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class FirebaseAuthService {

    // Get UID from email
    public static String getUidByEmail(String email) {
        try {
            UserRecord userRecord = InitializeFirbase.getAuth().getUserByEmail(email);
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            System.out.println("❌ Error fetching UID: " + e.getMessage());
            return null;
        }
    }

    // Delete user from Firebase Authentication
    public static boolean deleteUserByEmail(String email) {
        String uid = getUidByEmail(email);
        if (uid != null) {
            try {
                InitializeFirbase.getAuth().deleteUser(uid);
                System.out.println("✅ Auth user deleted: " + uid);
                return true;
            } catch (FirebaseAuthException e) {
                System.out.println("❌ Error deleting user: " + e.getMessage());
            }
        } else {
            System.out.println("⚠ UID not found for email: " + email);
        }
        return false;
    }
}