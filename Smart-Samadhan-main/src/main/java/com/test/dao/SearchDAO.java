package com.test.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.test.model.UserSearchResult; // We will create this model next
import com.test.servies.InitializeFirbase;

import java.util.ArrayList;
import java.util.List;

public class SearchDAO {

    public List<UserSearchResult> searchForUsers(String searchText) {
        List<UserSearchResult> results = new ArrayList<>();
        if (searchText == null || searchText.trim().isEmpty()) {
            return results;
        }

        Query usersQuery = InitializeFirbase.db.collection("users")
                .orderBy("userName")
                .startAt(searchText)
                .endAt(searchText + '\uf8ff');

        ApiFuture<QuerySnapshot> future = usersQuery.get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                String userId = doc.getId();
                
                results.add(new UserSearchResult(
                        userId,
                        doc.getString("firstName") + " " + doc.getString("lastName"),
                        doc.getString("userName"),
                        doc.getString("imageUrl")
                ));
            }
        } catch (Exception e) {
            System.err.println("❌ Error searching for users:");
            e.printStackTrace();
        }
        
        System.out.println("Found " + results.size() + " user(s) for query: " + searchText);
        return results;
    }
}