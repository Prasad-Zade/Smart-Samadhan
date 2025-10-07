package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

import com.test.model.Comment;
import com.test.servies.InitializeFirbase;

import java.util.HashMap;
import java.util.Map;

public class CommentDAO {
    private final Firestore db = InitializeFirbase.getdb();
    private static final String POSTS_COLLECTION = "posts";
    private static final String COMMENTS_COLLECTION = "comments";

    public ApiFuture<QuerySnapshot> getCommentsForPost(String postId) {
        return db.collection(POSTS_COLLECTION)
                .document(postId)
                .collection(COMMENTS_COLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get();
    }

    public ApiFuture<DocumentReference> addComment(String postId, Comment comment) {
        CollectionReference commentsRef = db.collection(POSTS_COLLECTION)
                .document(postId)
                .collection(COMMENTS_COLLECTION);

        // Create a map from the comment object to store in Firestore
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("userId", comment.getUserId());
        commentData.put("username", comment.getUsername());
        commentData.put("text", comment.getText());
        commentData.put("timestamp", comment.getTimestamp());
    
        return commentsRef.add(commentData);
    }
}