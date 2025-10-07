package com.test.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.test.Controller.PerplexityNewsController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatModel {
    PerplexityNewsController news=new PerplexityNewsController();

    public String getResponse(String userInput, String currentUserEmail) {
        if (userInput.toLowerCase().contains("status")) {
            return fetchComplaintStatus(currentUserEmail);
        }else if (userInput.toLowerCase().contains("hello") || userInput.toLowerCase().contains("hi") || userInput.toLowerCase().contains("hey")) {
            return "How can I assist you today?";
        } else if (userInput.toLowerCase().contains("thank")) {
            return "You're welcome! Happy to help.";
        } else if (userInput.toLowerCase().contains("report issue") || userInput.toLowerCase().contains("raise complaint") || userInput.toLowerCase().contains("file complaint")) {
            return "You can raise a new report by going to the 'Create Post' section.";
        } else if (userInput.toLowerCase().contains("update profile")) {
            return "You can update your profile from the 'Profile' page.";
        } else if (userInput.toLowerCase().contains("location") || userInput.toLowerCase().contains("area")) {
            return "Please mention your exact location or area for more specific help.";
        } else if (userInput.toLowerCase().contains("help") || userInput.toLowerCase().contains("support")) {
            return "I'm here to assist you with complaint status, reporting issues, or any civic concerns.";
        } else if (userInput.toLowerCase().contains("news") || userInput.toLowerCase().contains("updates")) {
            return news.getCurrentCivicNews("Pune");
        } else if (userInput.toLowerCase().contains("language") || userInput.toLowerCase().contains("hindi") || userInput.toLowerCase().contains("marathi")) {
            return "Multilingual support is coming soon. Currently, I understand English best.";
        } else if(isCivicRelated(userInput)){
            return callGeminiAPI(userInput);
        }else if(userInput.toLowerCase().contains("post")|| userInput.toLowerCase().contains("report form")||userInput.toLowerCase().contains("report")){
            return "Go to create Post section";
        } else if(userInput.toLowerCase().contains("message")|| userInput.toLowerCase().contains("chat")){
            return "To chat search the profile and click on message";
        }else if(userInput.toLowerCase().contains("No water supply")|| userInput.toLowerCase().contains("Drainage problem")){
            return callGeminiAPI(userInput);
        }else {
            return "I can only help with civic-related issues like garbage, potholes, drainage, and water problems.";
        }
    }

    private String fetchComplaintStatus(String email) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> query = db.collection("reports")
                .whereEqualTo("email", email)
                .get();

            QuerySnapshot querySnapshot = query.get();

            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                String firstName = document.getString("firstName");
                String lastName = document.getString("lastName");
                String issueType = document.getString("issueType");
                String description = document.getString("description");
                String location = document.getString("location");
                String date = document.getString("date");
                String reportId = document.getString("reportId");
                String status = document.getString("status");

                return String.format(
                    "Hello %s %s,\nYour complaint about '%s' at %s (Reported on %s) is:\n\"%s\".\nReport ID: %s\nStatus: %s",
                    firstName, lastName, issueType, location, date, description, reportId, status
                );
            } else {
                return "No complaint found with your email.";
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Failed to retrieve your complaint status. Please try again later.";
        }
    }

    public String callGeminiAPI(String prompt) {
    try {
        URL url = new URL("");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String instruction = "You are a civic help assistant. Only answer questions related to civic problems such as potholes, garbage collection, water supply, electricity issues, sanitation, drainage, and complaints to the municipal authority. If the query is not related to civic issues, politely say you can't help with that.";

        String fullPrompt = instruction + "\nUser: " + prompt;

        String body = """
        {
            "contents": [{
                "parts": [{
                    "text": "%s"
                }]
            }]
        }
        """.formatted(fullPrompt);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        JSONObject response = new JSONObject(sb.toString());
        JSONArray candidates = response.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");

        String responseText = parts.getJSONObject(0).getString("text");

        responseText = responseText.replace("*", "");
        responseText = responseText.replace("_", "").replace("`", "");

        return responseText;

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't get an answer right now. Please try again later.";
        }
    }

    private boolean isCivicRelated(String input) {
    String[] keywords = {"pothole", "garbage", "drainage", "water", "sanitation", "sewage", "leakage", "electricity", "civic", "complaint", "municipal"};
    input = input.toLowerCase();
        for (String keyword : keywords) {
            if (input.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
