package com.test.dao;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.test.model.UpdatedProfileModel;
import com.test.servies.InitializeFirbase;

public class UpdatedProfileData {

    public static void updateUserData(String email, UpdatedProfileModel model) {
        try {
            DocumentReference docRef = InitializeFirbase.db.collection("users").document(email);

            Map<String, Object> updates = new HashMap<>();
            updates.put("firstName", model.getFirstName());
            updates.put("lastName", model.getLastName());
            updates.put("userName", model.getUsername());
            updates.put("genderdetails", model.getGender());
            updates.put("email", model.getEmail());
            updates.put("address", model.getAddress());
            updates.put("phoneNo", model.getPhoneNo());
            updates.put("DOB", model.getDob());
            updates.put("location", model.getLocation());
            updates.put("postalCode", model.getPostalCode());

            ApiFuture<WriteResult> result = docRef.update(updates);
            System.out.println("Update time : " + result.get().getUpdateTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
