package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.test.model.Notification;
import com.test.servies.InitializeFirbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PostDAO {

    private final NotificationDAO notificationDAO;

    public PostDAO() {
        this.notificationDAO = new NotificationDAO();
    }

    public void createPost(File imageFile, String location, String description, String userEmail) {
        try {
            String imageUrl = uploadImage(imageFile);

            Map<String, Object> postData = new HashMap<>();
            postData.put("imageUrl", imageUrl);
            postData.put("location", location);
            postData.put("description", description);
            postData.put("timestamp", FieldValue.serverTimestamp());
            postData.put("username", ProfileDetailData.getUserName());
            postData.put("profileImage", ProfileDetailData.getImgProfile());
            postData.put("likeCount", 0);
            postData.put("likes", new HashMap<>());

            // CORRECTED: Changed getUserEmail() to getUID() which returns the email in your class.
            postData.put("userEmail", ProfileDetailData.getUID());

            DocumentReference postRef = InitializeFirbase.db.collection("posts").document();
            ApiFuture<WriteResult> future = postRef.set(postData);
            future.get();
            String postId = postRef.getId();
            System.out.println("✅ Post created with user info: " + postId);

            DocumentReference userRef = InitializeFirbase.db.collection("users").document(userEmail);
            userRef.update("userPosts", FieldValue.arrayUnion(postId));
            System.out.println("✅ Post ID added to user: " + userEmail);

        } catch (Exception e) {
            System.err.println("❌ Error creating post: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void likePost(String postId, String userId) {
        DocumentReference postRef = InitializeFirbase.db.collection("posts").document(postId);
        WriteBatch batch = InitializeFirbase.db.batch();

        batch.update(postRef, "likeCount", FieldValue.increment(1));
        batch.update(postRef, "likes." + userId, true);

        try {
            batch.commit().get();
            System.out.println("✅ Post " + postId + " liked by " + userId);
        } catch (Exception e) {
            System.err.println("❌ Error liking post: " + e.getMessage());
        }

        new Thread(() -> {
            try {
                ApiFuture<DocumentSnapshot> postFuture = postRef.get();
                DocumentSnapshot postDocument = postFuture.get();

                if (postDocument.exists()) {
                    String authorEmail = postDocument.getString("userEmail");
                    String likerUsername = ProfileDetailData.getFirstName();
                    String likerId = ProfileDetailData.getUID();

                    // CORRECTED: Changed getUserEmail() to getUID() for the comparison.
                    if (authorEmail != null && !authorEmail.equals(ProfileDetailData.getUID())) {

                        String message = (likerUsername != null ? likerUsername : "Someone") + " liked your post.";
                        Notification notification = new Notification(message, likerId, postId);

                        notificationDAO.createNotification(authorEmail, notification);
                        System.out.println("✅ Notification created for user: " + authorEmail);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("❌ Error creating notification: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void unlikePost(String postId, String userId) {
        DocumentReference postRef = InitializeFirbase.db.collection("posts").document(postId);
        WriteBatch batch = InitializeFirbase.db.batch();

        batch.update(postRef, "likeCount", FieldValue.increment(-1));
        batch.update(postRef, "likes." + userId, FieldValue.delete());

        try {
            batch.commit().get();
            System.out.println("✅ Post " + postId + " unliked by " + userId);
        } catch (Exception e) {
            System.err.println("❌ Error unliking post: " + e.getMessage());
        }
    }

    private String uploadImage(File file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String objectName = "posts/" + UUID.randomUUID().toString() + "-" + file.getName();

        try (InputStream fileStream = new FileInputStream(file)) {
            Blob blob = bucket.create(objectName, fileStream, "image/jpeg");
            return blob.signUrl(36500, TimeUnit.DAYS).toString();
        }
    }

    public void savePost(String postId, String userId) {
        InitializeFirbase.getdb().collection("users").document(userId)
                .update("savedPosts", com.google.cloud.firestore.FieldValue.arrayUnion(postId));
    }

    public void unsavePost(String postId, String userId) {
        InitializeFirbase.getdb().collection("users").document(userId)
                .update("savedPosts", com.google.cloud.firestore.FieldValue.arrayRemove(postId));
    }
}