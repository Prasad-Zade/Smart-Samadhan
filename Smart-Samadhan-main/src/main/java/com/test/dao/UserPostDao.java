package com.test.dao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.test.model.Post;
import com.test.servies.InitializeFirbase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class UserPostDao {

    public static ObservableList<Post> fetchPostsByIds(List<String> postIds, String currentUserId) {
        ObservableList<Post> posts = FXCollections.observableArrayList();
        if (postIds == null || postIds.isEmpty()) {
            return posts;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String postId : postIds) {
            try {
            
                DocumentSnapshot doc = InitializeFirbase.db.collection("posts").document(postId).get().get();

                if (doc.exists()) {
            
                    Timestamp ts = doc.getTimestamp("timestamp");
                    String formattedDate = (ts != null) ? sdf.format(ts.toDate()) : "No date";

                    long likeCount = doc.contains("likeCount") ? doc.getLong("likeCount") : 0;

                    Post post = new Post(
                            postId,
                            doc.getString("username"),
                            doc.getString("description"),
                            doc.getString("imageUrl"),
                            doc.getString("location"),
                            formattedDate,
                            doc.getString("profileImage"),
                            (int) likeCount
                    );

                    if (currentUserId != null && doc.contains("likes") && doc.get("likes") instanceof Map) {
                        Map<String, Object> likesMap = (Map<String, Object>) doc.get("likes");
                        if (likesMap.containsKey(currentUserId)) {
                            post.setLikedByCurrentUser(true);
                        }
                    }
                    posts.add(post);

                } else {
                    System.err.println("DAO_WARNING: Post with ID '" + postId + "' not found.");
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO_ERROR: Failed to fetch post with ID '" + postId + "'.");
                e.printStackTrace();
            }
        }
        return posts;
    }
}