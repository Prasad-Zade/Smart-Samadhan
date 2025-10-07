package com.test.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.test.servies.InitializeFirbase;

public class UserAuthController {

    public String sendPasswordResetEmail(String email) {
        try {
            
            FirebaseAuth auth = InitializeFirbase.getAuth();
            
            
            auth.generatePasswordResetLink(email);
            
            System.out.println("✅ Successfully sent password reset link to: " + email);
            return "SUCCESS";
            
        } catch (FirebaseAuthException e) {
            String errorCode = e.getAuthErrorCode().toString();
            System.err.println("❌ Error sending password reset email: " + errorCode + " - " + e.getMessage());
            return errorCode;
        }
    }
}