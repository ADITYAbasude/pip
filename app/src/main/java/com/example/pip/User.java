package com.example.pip;

public class User {
    public String User_Profile_Image_Uri, email, password, usName, pipPostData, pipuserName, like, date, usercomments, commentPostUserName, Bio, Location, Website, dateOfBirth;

    public User() {

    }

    public User(String User_Profile_Image_Uri){
        this.User_Profile_Image_Uri = User_Profile_Image_Uri;
    }


    public User(String email, String password, String Uername) {
        this.email = email;
        this.password = password;
        this.usName = Uername;
    }

    public User(String pipPostData, String pipuserName, String like, String date) {
        this.pipPostData = pipPostData;
        this.pipuserName = pipuserName;
        this.like = like;
        this.date = date;

    }

    public User(String comments, String commentPostUserName) {
        this.usercomments = comments;
        this.commentPostUserName = commentPostUserName;
    }

    public User(String Bio, String Location, String Website, String dateOfBirth , String Null) {
        this.Bio = Bio;
        this.Location = Location;
        this.Website = Website;
        this.dateOfBirth = dateOfBirth;
    }

//    public String getUsName() {
//        return usName;
//    }
}


