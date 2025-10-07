package com.test.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Application;

public class ApiController{

    public JSONArray getNews(String category){

        int page = 1;
        category = category == null || category.isEmpty() ? "general" : category;
        try{    
            String news_article = "https://newsapi.org/v2/everything?q=" + category +"&page=" + page + "";
            System.out.println(news_article);
            URL news_url = new URL(news_article);

            HttpURLConnection conn = (HttpURLConnection) news_url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //System.out.println(response);
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");
            return articles;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    } 
}