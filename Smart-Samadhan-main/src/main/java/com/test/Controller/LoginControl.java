package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class LoginControl {
    private static final String API_KEY="";

    public String LogInWithGmailAndPassword(String gmail, String password){
        try{
            URL url=new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String payload=String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                                        gmail,password);
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());

            int responseCode=conn.getResponseCode();
            if(responseCode==200){
                System.out.println("Login successful.");
                try(BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()))){
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=br.readLine())!=null){
                        response.append(line);
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse.getString("idToken"); // Return the idToken
                }
            } else {
                System.out.println("Login failed. Response Code: " + responseCode);
                try(BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()))){
                    String line;
                    while((line=br.readLine())!=null){
                        System.out.println(line); // Print Firebase error message
                    }
                }
                return null; // Login failed
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public boolean changePassword(String idToken, String newPassword) {
        if (idToken == null || idToken.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            System.out.println("Error: Invalid ID Token or new password provided for password change.");
            return false;
        }

        try {
        
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:update?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format("{\"idToken\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                                            idToken, newPassword);

            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Password changed successfully.");
                return true;
            } else {
                System.out.println("Failed to change password. Response Code: " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line); // Print Firebase error message
                    }
                }
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}