package com.example.SplitItGo.Model;

import java.util.List;

public class PostMovie {

    public List<PostMovies> results;

    public static class PostMovies {

        public String username;
        public String first_name;
        public String last_name;
        public String email;
        public String password;
        public String phone_number;
        public String confirm_password;
        public String otp;

        public PostMovies(String username, String first_name, String last_name, String email, String password, String phone_number, String confirm_password) {
            this.username = username;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.phone_number = phone_number;
            this.confirm_password = confirm_password;
        }

        public PostMovies(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public PostMovies(String otp) {
            this.otp = otp;
        }
    }
    public List<PostMovies> getResults() {
        return results;
    }
}