package com.test.dao;

import java.util.Map;
import com.google.cloud.firestore.DocumentSnapshot;
import com.test.model.EditProfileModel;
import com.test.servies.InitializeFirbase;

public class EditProfileData {
    public static String firstName;
    public static String lastName;
    public static String username;
    public static String genderdetails;
    public static String Email;
    public static String Address;
    public static String phoneNo;
    public static String Dob;
    public static String Location;
    public static String PostalCode;
    public static String profileImg;

    public EditProfileModel getEditDetails(String email) {
    System.out.println("In DAO edit profile");
    System.out.println(email);
    try {
        DocumentSnapshot response = InitializeFirbase.db.collection("users").document(email).get().get();
        System.out.println(response.getData());

        Map<String, Object> responseData = response.getData();

        if (responseData == null) {
            System.out.println("No user found with this email.");
            return null;
        }

        EditProfileModel editProfileModel = new EditProfileModel();
        editProfileModel.setFirstName(responseData.get("firstName").toString());
        editProfileModel.setLastName(responseData.get("lastName").toString());
        editProfileModel.setuserName(responseData.get("userName").toString());
        editProfileModel.setGender(responseData.get("genderdetails").toString());
        editProfileModel.setEmail(responseData.get("email").toString());
        editProfileModel.setAddress(responseData.get("address").toString());
        editProfileModel.setPhoneNumber(responseData.get("phoneNo").toString());
        editProfileModel.setDOB(responseData.get("DOB").toString());
        editProfileModel.setLocation(responseData.get("location").toString());
        editProfileModel.setPostalCode(responseData.get("postalCode").toString());
        editProfileModel.setProfile(responseData.get("imageUrl").toString());

        firstName = responseData.get("firstName").toString();
        lastName=responseData.get("lastName").toString();
        username=responseData.get("userName").toString();
        genderdetails=responseData.get("genderdetails").toString();
        Email=responseData.get("email").toString();
        Address=responseData.get("address").toString();
        phoneNo=responseData.get("phoneNo").toString();
        Dob=responseData.get("DOB").toString();
        Location=responseData.get("location").toString();
        PostalCode=responseData.get("postalCode").toString();
        profileImg=responseData.get("imageUrl").toString();

        return editProfileModel;

    } catch (Exception e) {
        System.out.println("Failed to fetch data...!");
        e.printStackTrace();
        return null;
    }
}

}
