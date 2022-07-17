package com.example.pip.Models;

public class FollowModel {
    public String follower_uid;

    public FollowModel() {
    }

    public FollowModel(String follower_uid) {
        this.follower_uid = follower_uid;
    }

    public String getFollower_uid() {
        return follower_uid;
    }
}
