package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.test.model.CityComplaintModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class CityComplaintDAO {

    public List<CityComplaintModel> getCityComplaintCounts() {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Integer> cityCountMap = new HashMap<>();
        Map<String, String> displayNameMap = new HashMap<>();

        try {
            // From "reports"
            ApiFuture<QuerySnapshot> future1 = db.collection("reports").get();
            List<QueryDocumentSnapshot> docs1 = future1.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs1) {
                String city = doc.getString("location");
                if (city != null) {
                    String key = city.trim().toLowerCase(); // Normalize
                    cityCountMap.put(key, cityCountMap.getOrDefault(key, 0) + 1);
                    displayNameMap.putIfAbsent(key, city.trim()); // Preserve first encountered display name
                }
            }

            // // From "posts"
            // ApiFuture<QuerySnapshot> future2 = db.collection("posts").get();
            // List<QueryDocumentSnapshot> docs2 = future2.get().getDocuments();
            // for (QueryDocumentSnapshot doc : docs2) {
            //     String city = doc.getString("location");
            //     if (city != null) {
            //         String key = city.trim().toLowerCase(); // Normalize
            //         cityCountMap.put(key, cityCountMap.getOrDefault(key, 0) + 1);
            //         displayNameMap.putIfAbsent(key, city.trim()); // Preserve first encountered display name
            //     }
            // }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<CityComplaintModel> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cityCountMap.entrySet()) {
            String displayName = displayNameMap.get(entry.getKey());
            result.add(new CityComplaintModel(displayName, entry.getValue()));
        }

        return result;
    }
}
