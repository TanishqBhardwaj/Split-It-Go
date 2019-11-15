package com.example.SplitItGo.Model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GetUsersResponse {
    @SerializedName("results")
    public ArrayList<UserData> results;

    public class UserData {
        @SerializedName("url")
        public String url;

        @SerializedName("username")
        public String username;

        @SerializedName("first_name")
        public String first_name;

        @SerializedName("last_name")
        public String last_name;

        @SerializedName("email")
        public String email;

        @SerializedName("phone_number")
        public String phone_number;

        @SerializedName("password")
        public String password;

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

    }

    public ArrayList<UserData> getResults() {
        return results;
    }
}
