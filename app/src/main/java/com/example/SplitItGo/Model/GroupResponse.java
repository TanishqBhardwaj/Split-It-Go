package com.example.SplitItGo.Model;

import java.util.ArrayList;

public class GroupResponse {

    private int id;
    private String name;
    private int admin;
    private ArrayList<Integer> users;
    private String type;
    private String created_at;

    public GroupResponse(int id, String name, int admin, ArrayList<Integer> users, String type, String created_at) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.users = users;
        this.type = type;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAdmin() {
        return admin;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }
}
