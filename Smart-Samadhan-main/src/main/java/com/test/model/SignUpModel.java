package com.test.model;

import java.util.HashMap;
import java.util.Map;

public class SignUpModel {
    
    String userName;
    String userEmail;
    String password;

    public SignUpModel(String userName,String userEmail,String password){

        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
    }

    public Map<String,String> getMap(){

        HashMap<String,String> data = new HashMap<String,String>();
        
        data.put("userName", userName);
        data.put("userEmail", userEmail);
        data.put("password", password);
        return data;
    } 
}
