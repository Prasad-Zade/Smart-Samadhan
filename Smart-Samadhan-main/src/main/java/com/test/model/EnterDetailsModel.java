package com.test.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EnterDetailsModel {
    String firstName, lastName, username, genderdetails, Email, Address, phoneNo, Dob, Location, PostalCode;
    File profileImageFile;

    public EnterDetailsModel(String firstName, String lastName, String username, String genderdetails, String Email, String Address, String phoneNo, String Dob, String Location, String PostalCode, File profileImageFile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.genderdetails = genderdetails;
        this.Email = Email;
        this.Address = Address;
        this.phoneNo = phoneNo;
        this.Dob = Dob;
        this.Location = Location;
        this.PostalCode = PostalCode;
        this.profileImageFile = profileImageFile;
    }

    public File getProfileImageFile() {
        return profileImageFile;
    }

    public Map<String,Object> getMap(){
        HashMap<String,Object> data = new HashMap<>();
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("userName", username);
        data.put("genderdetails", genderdetails);
        data.put("email", Email);
        data.put("address", Address);
        data.put("phoneNo", phoneNo);
        data.put("DOB", Dob);
        data.put("location", Location);
        data.put("postalCode", PostalCode);
        return data;
    }
}
