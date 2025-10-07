package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.test.model.MessageModel;
import com.test.servies.InitializeFirbase;

import java.util.*;

public class MessageDAO {

    private static final Firestore db = InitializeFirbase.getdb();

    public static void sendMessage(String senderId, String receiverId, String message) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("senderId", senderId);
            data.put("receiverId", receiverId);
            data.put("message", message);
            data.put("timestamp", System.currentTimeMillis());

            db.collection("messages").add(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<MessageModel> getAllMessages(String user1Id, String user2Id) {
        List<MessageModel> messages = new ArrayList<>();
        try {
            
            ApiFuture<QuerySnapshot> future = db.collection("messages")
                    .whereIn("senderId", Arrays.asList(user1Id, user2Id))
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                String senderId = doc.getString("senderId");
                String receiverId = doc.getString("receiverId");

                if ((senderId.equals(user1Id) && receiverId.equals(user2Id)) ||
                    (senderId.equals(user2Id) && receiverId.equals(user1Id))) {

                    MessageModel msg = new MessageModel();
                    msg.setSenderId(senderId);
                    msg.setReceiverId(receiverId);
                    msg.setMessage(doc.getString("message"));

                    Object ts = doc.get("timestamp");
                    msg.setTimestamp(ts instanceof Long ? (Long) ts : 0L);

                    messages.add(msg);
                }
            }

            messages.sort(Comparator.comparingLong(MessageModel::getTimestamp));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
