package com.example.pip;

public class User {
    public String email , password , usName , pipPostData;

    public User(){

    }

    public User(String email , String password ,String Uername){
        this.email = email;
        this.password = password;
        this.usName = Uername;
    }

    public User (String pipPostData){
        this.pipPostData = pipPostData;
    }
}


