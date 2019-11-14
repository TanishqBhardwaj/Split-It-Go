package com.example.SplitItGo.Model;

import java.util.ArrayList;

public class PostGroup {

    String name;
    String admin;
    ArrayList<Integer> users;
    String type;

    public PostGroup(String name, String admin, ArrayList<Integer> users, String type) {
        this.name = name;
        this.admin = admin;
        this.users = users;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getAdmin() {
        return admin;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public String getType() {
        return type;
    }
}
