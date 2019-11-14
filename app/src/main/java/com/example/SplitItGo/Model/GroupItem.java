package com.example.SplitItGo.Model;

public class GroupItem {

    private String mGroupMemberName;
    private int imageView;
    private String user_id;

    public GroupItem(String mGroupMemberName, int imageView, String user_id) {
        this.mGroupMemberName = mGroupMemberName;
        this.imageView = imageView;
        this.user_id = user_id;
    }

    public String getmGroupMemberName() {
        return mGroupMemberName;
    }

    public int getImageView() {
        return imageView;
    }

    public String getUser_id() {
        return user_id;
    }
}