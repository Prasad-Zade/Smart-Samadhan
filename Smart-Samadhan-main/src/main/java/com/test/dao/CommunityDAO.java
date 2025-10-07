package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.test.model.Community;
import com.test.servies.InitializeFirbase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Data Access Object for all community-related Firestore operations.
 */
public class CommunityDAO {

    private static final Firestore db = InitializeFirbase.getdb();
    private static final String COLLECTION_NAME = "communities";

    public static void createCommunity(Community community) {
        try {
            db.collection(COLLECTION_NAME).add(community);
        } catch (Exception e) {
            System.err.println("Error creating community: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Community> getAllCommunities() {
        List<Community> communities = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                                            .orderBy("timestamp", Query.Direction.DESCENDING)
                                            .get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                communities.add(document.toObject(Community.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching communities: " + e.getMessage());
            e.printStackTrace();
        }
        return communities;
    }

    // --- Admin-Only Actions ---

    public static void inviteUserToCommunity(String communityId, String userId) {
        try {
            DocumentReference communityRef = db.collection(COLLECTION_NAME).document(communityId);
            communityRef.update("members", FieldValue.arrayUnion(userId));
        } catch (Exception e) {
            System.err.println("Error inviting user: " + e.getMessage());
        }
    }
    
    public static void removeMemberFromCommunity(String communityId, String userId) {
        try {
            DocumentReference communityRef = db.collection(COLLECTION_NAME).document(communityId);
            communityRef.update("members", FieldValue.arrayRemove(userId));
        } catch (Exception e) {
            System.err.println("Error removing member: " + e.getMessage());
        }
    }

    // --- Public Join Request Actions ---

    public static void requestToJoinCommunity(String communityId, String userId) {
        try {
            DocumentReference communityRef = db.collection(COLLECTION_NAME).document(communityId);
            communityRef.update("pendingMembers", FieldValue.arrayUnion(userId));
        } catch (Exception e) {
            System.err.println("Error requesting to join: " + e.getMessage());
        }
    }

    public static void acceptJoinRequest(String communityId, String userId) {
        try {
            DocumentReference communityRef = db.collection(COLLECTION_NAME).document(communityId);
            // Use a transaction to safely move user from 'pending' to 'members'
            db.runTransaction(transaction -> {
                transaction.update(communityRef, "pendingMembers", FieldValue.arrayRemove(userId));
                transaction.update(communityRef, "members", FieldValue.arrayUnion(userId));
                return null;
            });
        } catch (Exception e) {
            System.err.println("Error accepting join request: " + e.getMessage());
        }
    }
    
    public static void declineJoinRequest(String communityId, String userId) {
        try {
            DocumentReference communityRef = db.collection(COLLECTION_NAME).document(communityId);
            communityRef.update("pendingMembers", FieldValue.arrayRemove(userId));
        } catch (Exception e) {
            System.err.println("Error declining join request: " + e.getMessage());
        }
    }

    
}

