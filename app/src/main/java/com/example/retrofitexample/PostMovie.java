package com.example.retrofitexample;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostMovie {

    private List<PostMovies> results;

    public static class PostMovies {
//        private Double popularity;
//
//        @SerializedName("original_title")
//        private String title;
//
//        @SerializedName("release_date")
//        private String date;
//
//        public Double getPopularity() {
//            return popularity;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDate() {
//            return date;
//        }
//
//        public PostMovies(Double popularity, String title, String date) {
//            this.popularity = popularity;
//            this.title = title;
//            this.date = date;
//        }


//        private String url;
        private String username;
        private String email;
//        private List<> groups;

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public PostMovies(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    public List<PostMovies> getResults() {
        return results;
    }
}
