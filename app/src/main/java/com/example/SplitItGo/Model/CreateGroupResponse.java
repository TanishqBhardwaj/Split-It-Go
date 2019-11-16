package com.example.SplitItGo.Model;

public class CreateGroupResponse {

    String details;
    int group_id;

    public CreateGroupResponse(String details, int group_id) {
        this.details = details;
        this.group_id = group_id;
    }

    public String getDetails() {
        return details;
    }

    public int getGroup_id() {
        return group_id;
    }
}
