package com.test.servies;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.test.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.util.List;

public class FirestoreService {
    public static Task<ObservableList<Request>> createFetchReportsTask() {
        return new Task<>() {
            @Override
            protected ObservableList<Request> call() throws Exception {
                ObservableList<Request> reports = FXCollections.observableArrayList();
                Firestore db = InitializeFirbase.getdb();
                ApiFuture<QuerySnapshot> future = db.collection("reports").get();
                QuerySnapshot querySnapshot = future.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                for (QueryDocumentSnapshot document : documents) {
                    try {
                        reports.add(document.toObject(Request.class));
                    } catch (Exception e) {
                        System.err.println("Failed to map document: " + document.getId());
                        e.printStackTrace();
                    }
                }
                return reports;
            }
        };
    }
}