package com.example.pip;

public class imageModel {
    public String imagemodeluri;
    public imageModel(){

    }
    public imageModel(String imageUri){
        this.imagemodeluri = imageUri;
    }

    public String getImagemodeluri() {
        return imagemodeluri;
    }

    public void setImagemodeluri(String imagemodeluri) {
        this.imagemodeluri = imagemodeluri;
    }
}
