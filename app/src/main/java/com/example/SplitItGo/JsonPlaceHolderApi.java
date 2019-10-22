package com.example.SplitItGo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("users")
    Call<PostMovie> getPostMovies();

    @POST("users/")
    Call<PostMovie.PostMovies> signUpPost(@Body PostMovie.PostMovies post);

    @POST("token-auth/")
    Call<LoginResponse> loginPost(@Body PostMovie.PostMovies postMovies);
}