package com.test.model;

public class EditProfileModel {
    public String firstName;
    public String lastName;
    public String username;
    public String genderdetails;
    public String Email;
    public String Address;
    public String phoneNo;
    public String Dob;
    public String Location;
    public String PostalCode;
    public String profileImg;

    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }
 
    public void setuserName(String username){
        this.username = username;
    }

    public void setGender(String genderdetails){
        this.genderdetails = genderdetails;
    }

    public void setEmail(String Email){
        this.Email = Email;
    }

    public void setAddress(String Address){
        this.Address = Address;
    }

    public void setPhoneNumber(String phoneNo){
        this.phoneNo = phoneNo;
    }

    public void setDOB(String Dob){
        this.Dob = Dob;
    }

    public void setLocation(String Location) { 
        this.Location = Location; 
    }

    public void setPostalCode(String PostalCode){
        this.PostalCode = PostalCode;
    }

    public void setProfile(String profileImg){
        this.profileImg=profileImg;
    }
}
