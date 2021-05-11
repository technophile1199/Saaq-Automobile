package com.example.saaq_automobile.model;

import androidx.annotation.NonNull;

public class profileManagement
{
    public String userID;
    private String profilePic;
    private String Fname;
    private String Lname;
    private String Email;
    private String phoneNumber;

    public profileManagement() {
    }

    public profileManagement(String profilePic, String fname, String lname, String email, String phone) {
        this.profilePic = profilePic;
        Fname = fname;
        Lname = lname;
        Email = email;
        phoneNumber = phone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @NonNull
    @Override
    public String toString() {
        return "Photo : " +profilePic+ " FirstName: " +Fname+ " LastName: " +Lname+ " Email " +Email+ " Phone " +phoneNumber;
    }
}
