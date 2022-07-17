package com.example.pip.Models;

public class CommentModel {
    public String u_id , userComment , Image_uri , UserName;

    public CommentModel(){}

    public CommentModel(String u_id , String userComment , String Image_uri , String UserName){
        this.u_id = u_id;
        this.userComment = userComment;
        this.Image_uri = Image_uri;
        this.UserName = UserName;
    }
}
