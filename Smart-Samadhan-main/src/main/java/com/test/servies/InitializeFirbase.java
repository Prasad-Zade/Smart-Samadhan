package com.test.servies;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import java.io.FileInputStream;
import java.io.IOException;

public class InitializeFirbase {
    public static Firestore db;

    static {
        try {
            setUpFirebase();
        } catch (IOException e) {
            throw new RuntimeException("FATAL: Firebase SDK initialization failed.", e);
        }
    }

    public static void setUpFirebase() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream("")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("")
                .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            db = FirestoreClient.getFirestore();
        }
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static Firestore getdb() { return db; }
    public static Bucket getBucket() { return StorageClient.getInstance().bucket(); }
    public static CollectionReference getCollection(String collectionName) {
        return getdb().collection(collectionName);
    }
}

