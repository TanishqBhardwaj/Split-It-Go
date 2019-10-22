package com.example.SplitItGo;

import java.util.List;

public class PostMovie {

    private List<PostMovies> results;

    public static class PostMovies {

        private String username;
        private String first_name;
        private String last_name;
        private String email;
        private String password;
        private String phone_number;

        public String getUsername() {
            return username;
        }

        public PostMovies(String username, String first_name, String last_name, String email, String password, String phone_number) {
            this.username = username;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.phone_number = phone_number;
        }

        public PostMovies(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public List<PostMovies> getResults() {
        return results;
    }
}
