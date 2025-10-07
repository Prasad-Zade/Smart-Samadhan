package com.test.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.test.servies.InitializeFirbase;

public class EnterDetailData {

    public void addEnterDetailsData(Map<String,Object> data, String email){
        try {
            DocumentReference docId = InitializeFirbase.db.collection("users").document(email);
            docId.update(data);
            System.out.println("✅ User data updated successfully.");
        } catch(Exception e){
            System.err.println("❌ Failed to update user data!");
            e.printStackTrace();
        }
    }

    public void updateProfile(File imageFile, String email) {
        try {
            String imageUrl = uploadImage(imageFile);

            // Optional: save image URL in separate collection
            Map<String, Object> postData = new HashMap<>();
            postData.put("imageUrl", imageUrl);
            postData.put("timestamp", FieldValue.serverTimestamp());

            DocumentReference postRef = InitializeFirbase.db.collection("profile").document();
            ApiFuture<WriteResult> future = postRef.set(postData);
            future.get(); 

            DocumentReference userRef = InitializeFirbase.db.collection("users").document(email);
            Map<String, Object> update = new HashMap<>();
            update.put("profileId", postRef.getId());
            update.put("imageUrl", imageUrl); 
            userRef.update(update).get();

            System.out.println("✅ Profile image uploaded and URL updated.");
        } catch (Exception e) {
            System.err.println("❌ Error uploading profile image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String uploadImage(File file) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        String objectName = "profile/" + UUID.randomUUID().toString() + "-" + file.getName();

        try (InputStream fileStream = new FileInputStream(file)) {
            Blob blob = bucket.create(objectName, fileStream, "image/jpeg");
            return blob.signUrl(36500, TimeUnit.DAYS).toString(); // 100 years
        }
    }
}
