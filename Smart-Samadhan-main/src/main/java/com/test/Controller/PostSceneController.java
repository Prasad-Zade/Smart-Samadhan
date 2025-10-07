package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate; // Added for date handling

import org.json.JSONArray;
import org.json.JSONObject;

public class PostSceneController {

    String API_Key = "";

    public String aiPrompString(String locationText, String descriptionText, String dateText) {

        // Determine the final date, using today if the input is empty.
        String finalDate = (dateText == null || dateText.trim().isEmpty())
            ? LocalDate.now().toString()  // ISO format: YYYY-MM-DD
            : dateText;

        try {
            URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_Key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            
            String prompt = String.format(
                
                "Assume the role of a helpful assistant whose only job is to write a formal civic complaint. Your goal is to always try to create a full report from the user's input, even if the details are brief.\n\n" +

                "Using the provided 'Location', 'Original Description', and 'Date', you must expand upon them to write a complete and formal paragraph. This single paragraph must be written in the first person and seamlessly integrate:\n" +
                "- A formal description of the issue.\n" +
                "- The most suitable category (e.g., Roads, Drainage, Electricity).\n" +
                "- The potential risk or danger involved.\n" +
                "- The date of observation.\n\n" +

                "There is only one rule for failure: if the 'Location' or 'Original Description' field is completely empty, then your entire response must be the exact sentence: 'Please provide both a specific location and a detailed description to proceed.'\n\n" +
                "For all non-empty inputs, you must generate the full report. Do not ask for more information in your response. Do not use placeholders like '[details]'. The output must be a single block of plain text with no numbering or markdown.\n\n" +

                "Location: %s\n" +
                "Original Description: %s\n" +
                "Date: %s",

                locationText.replace("\"", "\\\""),
                descriptionText.replace("\"", "\\\""),
                finalDate
            );

            String payload = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

            
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                JSONObject firstPart = parts.getJSONObject(0);
                String generatedText = firstPart.getString("text");

                return generatedText.trim(); 

            } else {
                
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line.trim());
                }
                br.close();
                return "Error " + responseCode + ": " + errorResponse.toString();
            }

        } catch (Exception exception) {
            return "Exception: " + exception.getMessage();
        }
    }

    public String translateText(String inputText, String targetLanguage) {
        int maxRetries = 3;
        int retryDelay = 2000;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_Key);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                inputText = inputText.replace("\"", "\\\"").replace("\n", " ").trim();

                String prompt = String.format(
                    "Translate the following text to %s without changing its meaning:\n%s",
                    targetLanguage, inputText
                );

                String payload = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes("UTF-8"));
                }

                int responseCode = conn.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                }

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                if (responseCode == 503) {
                    System.out.println("Model is overloaded. Retrying... (" + attempt + ")");
                    Thread.sleep(retryDelay); 
                    continue; 
                }

                if (responseCode != 200) {
                    System.err.println("Translation Error Response: " + response);
                    return inputText;
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                JSONObject firstPart = parts.getJSONObject(0);

                return firstPart.getString("text").trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return inputText;
    }
}