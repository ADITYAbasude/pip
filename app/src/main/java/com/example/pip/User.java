package com.example.pip;

public class User {
    public String email, password, usName, pipPostData , pipuserName  ,  like , date ,comments;

    public User() {

    }

    public User(String email, String password, String Uername) {
        this.email = email;
        this.password = password;
        this.usName = Uername;
    }

    public User(String pipPostData , String pipuserName , String like , String date ) {
        this.pipPostData = pipPostData;
        this.pipuserName = pipuserName;
        this.like = like;
        this.date = date;

    }
    public User(String comments){
        this.comments = comments;
    }

    public String getUsName() {
        return usName;
    }
}


