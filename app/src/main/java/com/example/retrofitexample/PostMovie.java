package com.example.retrofitexample;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostMovie {

    private List<PostMovies> results;

    public class PostMovies {
        private double popularity;

        @SerializedName("original_title")
        private String title;

        @SerializedName("release_date")
        private String date;

        public double getPopularity() {
            return popularity;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }
    }

    public List<PostMovies> getResults() {
        return results;
    }
}
