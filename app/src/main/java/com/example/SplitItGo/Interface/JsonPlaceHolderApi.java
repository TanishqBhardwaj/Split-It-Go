package com.example.SplitItGo.Interface;

import com.example.SplitItGo.Model.GetUsersResponse;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.Model.LoginResponse;
import com.example.SplitItGo.Model.OtpResponse;
import com.example.SplitItGo.Model.PostGroup;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.Model.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("users/")
    Call<GetUsersResponse> getGroupMembers();

    @POST("signup/")
    Call<SignUpResponse> signUpPost(@Body PostMovie.PostMovies postMovies);

    @POST("login/")
    Call<LoginResponse> loginPost(@Body PostMovie.PostMovies postMovies);

    @POST("validate/{id}/")
    Call<OtpResponse> otpVerification(@Path("id") String userId, @Body PostMovie.PostMovies postMovies);

    @POST("profile/2/create-group/")
    Call<GroupResponse> createGroup( @Body PostGroup postGroup);
}