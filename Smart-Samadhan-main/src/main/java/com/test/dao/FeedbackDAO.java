package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.test.model.FeedbackModel;
import com.test.servies.InitializeFirbase;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class FeedbackDAO {

    private static final Firestore db = InitializeFirbase.getdb();

    public static void submitFeedback(String name, String feedbackText, int rating) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("feedback", feedbackText);
            data.put("rating", rating);
            data.put("timestamp", System.currentTimeMillis());

            db.collection("feedbacks").add(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FeedbackModel> getAllFeedbacks() {
        List<FeedbackModel> list = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("feedbacks").orderBy("timestamp", Query.Direction.DESCENDING).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                FeedbackModel feedback = new FeedbackModel();
                feedback.setName(doc.getString("name"));
                feedback.setFeedback(doc.getString("feedback"));
                feedback.setRating(doc.getLong("rating").intValue());
                feedback.setTimestamp(doc.getLong("timestamp"));
                list.add(feedback);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
