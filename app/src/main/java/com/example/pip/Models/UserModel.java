package com.example.pip.Models;

public class UserModel {
    public String pipImage, pip_id, User_Profile_Image_Uri, email, password, usName, pipPostData, pipuserName, like, date, usercomments, commentPostUserName, Bio, Location, Website, dateOfBirth;

    public UserModel() {}

    public UserModel(String User_Profile_Image_Uri){
        this.User_Profile_Image_Uri = User_Profile_Image_Uri;
    }


    public UserModel(String email , String password, String userName) {
        this.email = email;
        this.password = password;
        this.usName = userName;
    }

    public UserModel(String pipPostData, String pipuserName, String pip_id, String date , String pipImage) {
        this.pipPostData = pipPostData;
        this.pipuserName = pipuserName;
        this.pip_id = pip_id;
        this.date = date;
        this.pipImage = pipImage;

    }

    public UserModel(String comments, String commentPostUserName) {
        this.usercomments = comments;
        this.commentPostUserName = commentPostUserName;
    }

    public UserModel(String Bio, String Location, String Website, String dateOfBirth ) {
        this.Bio = Bio;
        this.Location = Location;
        this.Website = Website;
        this.dateOfBirth = dateOfBirth;
    }


}


