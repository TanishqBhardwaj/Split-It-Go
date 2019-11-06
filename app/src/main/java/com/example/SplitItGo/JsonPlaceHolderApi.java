package com.example.SplitItGo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @POST("signup/")
    Call<SignUpResponse> signUpPost(@Body PostMovie.PostMovies postMovies);

    @POST("token-auth/")
    Call<LoginResponse> loginPost(@Body PostMovie.PostMovies postMovies);

    @POST("validate/{id}/")
    Call<OtpResponse> otpVerification(@Path("id") String userId, @Body PostMovie.PostMovies postMovies);
}