package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class PerplexityNewsController {

    private final String PERPLEXITY_API_KEY = "";

    public String getCurrentCivicNews(String location) {
    
        try {
            URL url = new URL("https://api.perplexity.ai/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + PERPLEXITY_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", 
                "You are a highly specialized Civic News Bot. Your sole purpose is to provide recent, relevant civic news for a specified geographical location. Follow these rules strictly:\n" +
                "1. **Validate Input:** First, you MUST analyze the user's location input. Determine if it is a real, recognized city, state, or major metropolitan area.\n" +
                "2. **Handle Invalid Input:** If the input is NOT a valid geographical location (e.g., it is a school, a specific building, a temple, an organization, or nonsensical text), you MUST stop immediately and respond with ONLY the following exact string: 'ERROR: Please provide a valid city or major metropolitan area.'\n" +
                "3. **Define Civic News:** If the location is valid, find civic news. Civic news includes: public transport updates, new infrastructure projects, local government announcements, water/power supply issues, sanitation, and urban planning. Strictly EXCLUDE: national politics, crime reports, celebrity news, and sports.\n" +
                "4. **Format Output:** Provide the top 2-3 most recent civic news items. Summarize each one concisely and clearly. Mention the date of the news.\n" + 
                "5. **Also mention dates of the news reported and news must be old of last 3 days from current day not more than that"
            );
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", String.format(
                "Give me the top 3 recent civic issues or news updates from %s. Be specific and summarize concisely.",
                location
            ));

            JSONArray messages = new JSONArray();
            messages.put(systemMessage);
            messages.put(userMessage);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "sonar-pro");
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            InputStreamReader isr = new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8);
            
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            if (responseCode != 200) {
                String errorDetails = "No specific error details available.";
                try {

                    JSONObject errorJson = new JSONObject(response.toString());
                    if (errorJson.has("error")) {
                         errorDetails = errorJson.getJSONObject("error").toString(2);
                    }
                } catch (Exception e) {
                
                    errorDetails = response.toString();
                }
                return String.format("Error %d: %s", responseCode, errorDetails);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("choices")) {
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices.length() > 0 && choices.getJSONObject(0).has("message")) {
                    JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                    if (message.has("content")) {
                        return message.getString("content").trim();
                    }
                }
            }
            return "Error: Could not parse a valid response from the API.";

        } catch (Exception e) {
            
            return "Exception occurred: " + e.getMessage();
        }
    }
}