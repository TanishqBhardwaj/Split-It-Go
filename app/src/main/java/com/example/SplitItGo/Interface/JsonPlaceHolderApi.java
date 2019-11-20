package com.example.SplitItGo.Interface;

import com.example.SplitItGo.Model.ExpensesResponse;
import com.example.SplitItGo.Model.GetUsersResponse;
import com.example.SplitItGo.Model.CreateGroupResponse;
import com.example.SplitItGo.Model.GroupResponse;
import com.example.SplitItGo.Model.LoginResponse;
import com.example.SplitItGo.Model.OtpResponse;
import com.example.SplitItGo.Model.PostExpense;
import com.example.SplitItGo.Model.PostGroup;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.Model.SignUpResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("users/")
    Call<GetUsersResponse> getGroupMembers(@Header("Authorization") String token);

    @POST("signup/")
    Call<SignUpResponse> signUpPost(@Body PostMovie.PostMovies postMovies);

    @POST("login/")
    Call<LoginResponse> loginPost(@Body PostMovie.PostMovies postMovies);

    @POST("validate/{id}/")
    Call<OtpResponse> otpVerification(@Path("id") String userId, @Body PostMovie.PostMovies postMovies);

    @POST("profile/create-group/")
    Call<CreateGroupResponse> createGroup(@Body PostGroup postGroup, @Header("Authorization") String token);

    @GET("profile/groups/")
    Call<List<GroupResponse>> getGroups(@Header("Authorization") String token);

    @GET("profile/groups/{id}/expenses/")
    Call<List<ExpensesResponse>> getExpenses(@Path("id") String groupId, @Header("Authorization") String token);

    @POST("profile/groups/{id}/create-expense/")
    Call<ExpensesResponse> createExpense(@Path("id") String groupId, @Header("Authorization") String token,
                                         @Body PostExpense postExpense);

    @GET("profile/friendlist/")
    Call<ArrayList<Integer>> getFriendList(@Header("Authorization") String token);

    @POST("profile/add_friend/{id}/")
    Call<String> addFriend(@Path("id") String userId, @Header("Authorization") String token);
}