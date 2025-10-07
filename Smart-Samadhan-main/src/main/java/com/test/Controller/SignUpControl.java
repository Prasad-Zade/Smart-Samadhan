package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpControl {
        private static final String API_KEY="";
         
        public static String signInWithEmailAndPassword(String email, String password){
        
        if (email == null || email.trim().isEmpty()) {
            return "❌ Email is missing!";
        }

        if (password == null || password.trim().isEmpty()) {
            return "❌ Password is missing!";
        }

        StringBuilder errorBuilder = new StringBuilder("❌ Password issues:\n");

        boolean isValid = true;

        if (!password.matches(".*[A-Z].*")) {
            errorBuilder.append("- One uppercase letter is missing\n");
            isValid = false;
        }
        if (!password.matches(".*[a-z].*")) {
            errorBuilder.append("- One lowercase letter is missing\n");
            isValid = false;
        }
        if (!password.matches(".*\\d.*")) {
            errorBuilder.append("- One number is missing\n");
            isValid = false;
        }
        if (!password.matches(".*[@#$%^&+=!()_{}\\[\\]:;\"'<>,.?/~`|\\\\-].*")) {
            errorBuilder.append("- One special character is missing\n");
            isValid = false;
        }
        if (password.length() < 8) {
            errorBuilder.append("- Minimum 8 characters required\n");
            isValid = false;
        }

        if (!isValid) {
            return errorBuilder.toString();
        }


            try{
                URL url=new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                String payload=String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}"
                ,email,password);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes());
                }

                int responseCode=conn.getResponseCode();


                if(responseCode==200){
                    
                    return "success";
                }else{

                    StringBuilder errorMessage = new StringBuilder("❌ Signup failed. Reason: ");
                    try(BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()))){
                        String line;
                        while((line=br.readLine())!=null){
                            errorMessage.append(line);
                        }
                    }
                    return errorMessage.toString();
                }

                
            }catch(Exception ex){
                ex.printStackTrace();
                return "❌ An error occurred during signup.";
            }
       }
    
}
