package com.test.dao;

import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.test.servies.InitializeFirbase;


public class SignUpData  {
    String email;
    String userName;

    public boolean isUsernameTaken(String userName) {
        try {
            QuerySnapshot querySnapshot = InitializeFirbase.db.collection("users")
                .whereEqualTo("userName", userName)
                .get()
                .get();
                return !querySnapshot.isEmpty();
        } catch (Exception e) {
            System.out.println("Error while checking username: " + e.getMessage());
            e.printStackTrace();
            return true; // treat as taken if exception occurs
        }
    }


    public void addSignUpData(Map<String,String> data,String email) {
        this.email=email;
        try{

           DocumentReference docId =(DocumentReference) InitializeFirbase.db.collection("users").document(email);
           docId.set(data);

          
           System.out.println("Data added Succesfully ....!");
           Thread.sleep(5000);
           getUsers();

        } catch(Exception e){
            System.out.println("Failed to add data ...!");
            e.printStackTrace();
        }
    }

    private void getUsers(){
         try{

            QuerySnapshot response = (QuerySnapshot) InitializeFirbase.db.collection("users").get().get();
            
                System.out.println(email);
                for (QueryDocumentSnapshot document : response.getDocuments()) {

                    if(email.equals(document.getId())){

                        System.out.println("Document ID: " + document.getId());
                        System.out.println("Data: " + document.getData());
                        break;
                    } else{
                        System.out.println("Document not found ..!");
                        System.out.println("Document ID: " + document.getId());
                        System.out.println("Data: " + document.getData());
                    }
                }
          
           System.out.println("Data fetched Succesfully ....!");

        } catch(Exception e){
            System.out.println("Failed to fetch data ...!");
            e.printStackTrace();
        }
    }
}
