package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatBotController {

    private final String PERPLEXITY_API_KEY = "";

    public String getSmartSamadhanResponse(String userQuery) {
        if (PERPLEXITY_API_KEY == null || PERPLEXITY_API_KEY.trim().isEmpty()) {
            return "Error: Perplexity API key not set.";
        }

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
                "You are Smart Samadhan, a civic support chatbot. Your job is to answer citizen queries related to:\n" +
                "- Complaint status and resolution timelines\n" +
                "- Recent civic news (water, power, roads, public services)\n" +
                "- Guidelines on how to register civic complaints\n" +
                "- Updates about local municipal services\n\n" +
                "IMPORTANT RULES:\n" +
                "1. If the user asks about complaint status, reply: 'Please enter your complaint ID to check the current status.'\n" +
                "2. If the user gives a city name, return civic news for that city (public works, sanitation, roadwork, utilities) for the last 3 days. News should be location-specific.\n" +
                "3. Never give national politics, celebrity news, or sports updates.\n" +
                "4. If the city is invalid (e.g. a temple or school), return: 'ERROR: Please provide a valid city or major metropolitan area.'\n" +
                "5. Always respond politely and professionally as a civic assistant."
            );

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userQuery);

            JSONArray messages = new JSONArray();
            messages.put(systemMessage);
            messages.put(userMessage);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "sonar-pro");
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.5);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            InputStreamReader isr = new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8
            );

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            if (responseCode != 200) {
                String errorDetails = "Unable to fetch response.";
                try {
                    JSONObject errorJson = new JSONObject(response.toString());
                    if (errorJson.has("error")) {
                        errorDetails = errorJson.getJSONObject("error").toString(2);
                    }
                } catch (Exception e) {
                    errorDetails = response.toString();
                }
                return "API Error: " + errorDetails;
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

            return "Error: No valid response received.";

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}
