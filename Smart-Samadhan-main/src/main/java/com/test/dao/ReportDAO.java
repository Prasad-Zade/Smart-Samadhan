package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.test.servies.InitializeFirbase; // Your existing Firebase initialization class

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ReportDAO {

    private static final String COLLECTION_NAME = "reports";

    public void createReport(String userEmail, File imageFile, String firstName, String lastName,
                             String email, String phone, String title, String issueType,
                             String location, String date, String description) {
        try {
            Bucket bucket = InitializeFirbase.getBucket();
            String uniqueImageName = "report_images/" + UUID.randomUUID().toString();
            byte[] bytes = Files.readAllBytes(imageFile.toPath());
            Blob blob = bucket.create(uniqueImageName, bytes, "image/jpeg");
            String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() + "/o/" +
                              java.net.URLEncoder.encode(uniqueImageName, "UTF-8") + "?alt=media";

            String reportId = UUID.randomUUID().toString();
            Map<String, Object> reportData = new HashMap<>();
            reportData.put("reportId", reportId);
            reportData.put("firstName", firstName);
            reportData.put("lastName", lastName);
            reportData.put("email", email);
            reportData.put("phone", phone);
            reportData.put("title", title);
            reportData.put("issueType", issueType);
            reportData.put("location", location);
            reportData.put("date", date);
            reportData.put("description", description);
            reportData.put("imageUrl", imageUrl);
            reportData.put("userEmail", userEmail);
            reportData.put("timestamp", System.currentTimeMillis());
            reportData.put("status", "Pending");
            reportData.put("priority", "Medium");
            // Add 'assignedTo' field with a null or empty value initially
            reportData.put("assignedTo", "");


            DocumentReference reportRef = InitializeFirbase.getdb().collection(COLLECTION_NAME).document(reportId);
            ApiFuture<WriteResult> result = reportRef.set(reportData);
            result.get();

            DocumentReference userRef = InitializeFirbase.getdb().collection("users").document(userEmail);
            userRef.update("reports", com.google.cloud.firestore.FieldValue.arrayUnion(reportId));

            System.out.println("✅ Report submitted with default status and priority!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to submit report.");
        }
    }

    public void updateReportStatus(String reportId, String newStatus) {
        if (reportId == null || reportId.trim().isEmpty()) {
            System.err.println("❌ Cannot update report status. Report ID is null or empty.");
            return;
        }
        try {
            DocumentReference reportRef = InitializeFirbase.getdb().collection(COLLECTION_NAME).document(reportId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", newStatus);
            
            if ("Completed".equalsIgnoreCase(newStatus)) {
                updates.put("resolutionTimestamp", System.currentTimeMillis());
            } else {
                updates.put("resolutionTimestamp", null);
            }
            ApiFuture<WriteResult> result = reportRef.update(updates);
            result.get();
            System.out.println("✅ Report " + reportId + " status updated to: " + newStatus);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to update report status for ID: " + reportId);
        }
    }

    public void updateReportPriority(String reportId, String newPriority) {
        if (reportId == null || reportId.trim().isEmpty()) {
            System.err.println("❌ Cannot update report priority. Report ID is null or empty.");
            return;
        }
        try {
            DocumentReference reportRef = InitializeFirbase.getdb().collection(COLLECTION_NAME).document(reportId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("priority", newPriority);
            ApiFuture<WriteResult> result = reportRef.update(updates);
            result.get();
            System.out.println("✅ Report " + reportId + " priority updated to: " + newPriority);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to update report priority for ID: " + reportId);
        }
    }

    public void updateReportAssignedTo(String reportId, String guideId) {
        if (reportId == null || reportId.trim().isEmpty() || guideId == null) {
            System.err.println("❌ Cannot assign report. Report ID or Guide ID is null or empty.");
            return;
        }
        try {
            DocumentReference reportRef = InitializeFirbase.getdb().collection(COLLECTION_NAME).document(reportId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("assignedTo", guideId);
            updates.put("status", "Under Review"); // Automatically update status upon assignment

            ApiFuture<WriteResult> result = reportRef.update(updates);
            result.get();

            System.out.println("✅ Report " + reportId + " assigned to guide " + guideId + " and status set to 'Under Review'");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to assign report for ID: " + reportId);
        }
    }
}