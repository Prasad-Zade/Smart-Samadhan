package com.test.Controller;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TranslationService {

    private static final String API_Key = "";

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