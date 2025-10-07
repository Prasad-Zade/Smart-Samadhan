package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.test.model.CommunityMessageModel;
import com.test.servies.InitializeFirbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CommunityMessageDAO {
    
    private static final Firestore db = InitializeFirbase.getdb();
    private static final String COLLECTION_NAME = "community_messages";

    /**
     * Send a message to a community
     * @param communityId The ID of the community
     * @param senderId The ID of the user sending the message
     * @param senderName The display name of the sender
     * @param messageText The message content
     */
    public static void sendCommunityMessage(String communityId, String senderId, String senderName, String messageText) {
        try {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("communityId", communityId);
            messageData.put("senderId", senderId);
            messageData.put("senderName", senderName);
            messageData.put("message", messageText);
            messageData.put("timestamp", System.currentTimeMillis());
            
            db.collection(COLLECTION_NAME).add(messageData);
            System.out.println("[CommunityMessageDAO] Message sent by " + senderName + " to community " + communityId);
        } catch (Exception e) {
            System.err.println("Error sending community message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get all messages for a specific community
     * @param communityId The ID of the community
     * @return List of community messages ordered by timestamp
     */
    public static List<CommunityMessageModel> getCommunityMessages(String communityId) {
        List<CommunityMessageModel> messages = new ArrayList<>();
        
        try {
            // Temporary: Remove orderBy to avoid index requirement while it's building
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("communityId", communityId)
                    // .orderBy("timestamp", Query.Direction.ASCENDING) // Will add back when index is ready
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                CommunityMessageModel message = document.toObject(CommunityMessageModel.class);
                messages.add(message);
            }
            
            // Sort manually in Java since we can't use Firestore orderBy yet
            messages.sort((m1, m2) -> Long.compare(m1.getTimestamp(), m2.getTimestamp()));
            
            System.out.println("[CommunityMessageDAO] Retrieved " + messages.size() + " messages for community " + communityId);
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching community messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    /**
     * Get recent messages for a community (limit to last N messages)
     * @param communityId The ID of the community
     * @param limit Maximum number of messages to retrieve
     * @return List of recent community messages
     */
    public static List<CommunityMessageModel> getRecentCommunityMessages(String communityId, int limit) {
        List<CommunityMessageModel> messages = new ArrayList<>();
        
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("communityId", communityId)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(limit)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                CommunityMessageModel message = document.toObject(CommunityMessageModel.class);
                messages.add(message);
            }
            
            // Reverse the list to show oldest first
            messages.sort((m1, m2) -> Long.compare(m1.getTimestamp(), m2.getTimestamp()));
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching recent community messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    /**
     * Delete a community message by its ID
     * @param messageId The ID of the message to delete
     */
    public static void deleteCommunityMessage(String messageId) {
        try {
            db.collection(COLLECTION_NAME).document(messageId).delete();
            System.out.println("[CommunityMessageDAO] Community message deleted: " + messageId);
        } catch (Exception e) {
            System.err.println("Error deleting community message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Delete all messages for a specific community (useful when deleting a community)
     * @param communityId The ID of the community
     */
    public static void deleteAllCommunityMessages(String communityId) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("communityId", communityId)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
            
            System.out.println("[CommunityMessageDAO] Deleted all messages for community: " + communityId);
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting all community messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get message count for a community
     * @param communityId The ID of the community
     * @return Number of messages in the community
     */
    public static int getCommunityMessageCount(String communityId) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("communityId", communityId)
                    .get();

            return future.get().getDocuments().size();
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting community message count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}