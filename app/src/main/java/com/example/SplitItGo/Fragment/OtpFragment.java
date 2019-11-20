package com.example.SplitItGo.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.OtpResponse;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.RetrofitInstance;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {

    String mUserId;
    String username;
    EditText mEditTextOtp;
    Button mButtonSumbitOtp;
    Button mButtonBackOtp;
    String editTextOtp;
    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    public OtpFragment(String mUserId, String username) {
        this.mUserId = mUserId;
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_otp, container, false);

        mEditTextOtp = view.findViewById(R.id.editTextOtp);
        mButtonSumbitOtp = view.findViewById(R.id.buttonSubmitOtp);
        mButtonBackOtp = view.findViewById(R.id.buttonBackOtp);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        mButtonSumbitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextOtp = mEditTextOtp.getText().toString();
                otpVerification();
            }
        });

        mButtonBackOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new SignUpFragment()).commit();
            }
        });
        return view;
    }

    public void otpVerification() {
        PostMovie.PostMovies postMovies = new PostMovie.PostMovies(editTextOtp);

        Call<OtpResponse> call = jsonPlaceHolderApi.otpVerification(mUserId, postMovies);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                OtpResponse posts = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";

                Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                if(response.code()!= 404) {
                    if(posts.getMessage()!=null) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginFragment()).commit();
                    }
                    Toast.makeText(getContext(), posts.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {

            }
        });
    }
}
