package com.test.firebase;

public class FirebaseUtil {
    public static String getComplaintStatus(String userId) {
        return "Your pothole complaint is under review. PMC will resolve it in 2 days.";
    }

    public static String getComplaintSummary(String userId) {
        return "Pothole at MG Road raised on 20 July 2025.";
    }
}
