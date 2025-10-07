package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference; // Import DocumentReference
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.test.model.Notification;
import com.test.servies.InitializeFirbase;

public class NotificationDAO {

    private static final String USERS_COLLECTION = "users";
    private static final String NOTIFICATIONS_SUBCOLLECTION = "notifications";

    private final Firestore db;

    public NotificationDAO() {
        this.db = InitializeFirbase.getdb();
    }

    public ApiFuture<DocumentReference> createNotification(String recipientUserId, Notification notification) {
        if (recipientUserId == null || recipientUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient User ID cannot be null or empty.");
        }

        CollectionReference notificationsRef = db.collection(USERS_COLLECTION)
                                                 .document(recipientUserId)
                                                 .collection(NOTIFICATIONS_SUBCOLLECTION);

        return notificationsRef.add(notification);
    }

    public ApiFuture<QuerySnapshot> getNotificationsForUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        
        CollectionReference notificationsRef = db.collection(USERS_COLLECTION)
                                                 .document(userId)
                                                 .collection(NOTIFICATIONS_SUBCOLLECTION);

        return notificationsRef.orderBy("timestamp", Query.Direction.DESCENDING).get();
    }

    public ApiFuture<WriteResult> markNotificationAsRead(String userId, String notificationId) {
        if (userId == null || userId.trim().isEmpty() || notificationId == null || notificationId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID and Notification ID cannot be null or empty.");
        }

        return db.collection(USERS_COLLECTION)
                 .document(userId)
                 .collection(NOTIFICATIONS_SUBCOLLECTION)
                 .document(notificationId)
                 .update("read", true);
    }
}