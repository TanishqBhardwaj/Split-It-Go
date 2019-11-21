package com.example.SplitItGo.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Model.SignUpResponse;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private String mUserId;
    TextView textView;
    ImageView imageView;

    private TextInputEditText mEditTextUsernameSignUp;
    private TextInputEditText mEditTextFirstNameSignUp;
    private TextInputEditText mEditTextLastNameSignUp;
    private TextInputEditText mEditTextEmailSignUp;
    private TextInputEditText mEditTextPasswordSignUp;
    private TextInputEditText mEditTextConfirmPasswordSignUp;
    private TextInputEditText mEditTextPhoneNumberSignUp;

    private String editTextUsernameSignUpValue;
    private String editTextFirstNameSignUpValue;
    private String editTextLastNameSignUpValue;
    private String editTextEmailSignUpValue;
    private String editTextPasswordSignUpValue;
    private String editTextConfirmPasswordSignUpValue;
    private String editTextPhoneNumberSignUpValue;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        if(savedInstanceState != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new SignUpFragment()).commit();
        }

        mEditTextUsernameSignUp = view.findViewById(R.id.editTextUsernameSignUp);
        mEditTextFirstNameSignUp = view.findViewById(R.id.editTextFirstNameSignUp);
        mEditTextLastNameSignUp = view.findViewById(R.id.editTextLastNameSignUp);
        mEditTextEmailSignUp = view.findViewById(R.id.editTextEmailSignUp);
        mEditTextPasswordSignUp = view.findViewById(R.id.editTextPasswordSignUp);
        mEditTextConfirmPasswordSignUp = view.findViewById(R.id.editTextConfirmPasswordSignUp);
        mEditTextPhoneNumberSignUp = view.findViewById(R.id.editTextPhoneNumberSignUp);
        textView = view.findViewById(R.id.textViewSignUpLink);
        imageView = view.findViewById(R.id.imageView4);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextUsernameSignUpValue = mEditTextUsernameSignUp.getText().toString();
                editTextFirstNameSignUpValue = mEditTextFirstNameSignUp.getText().toString();
                editTextLastNameSignUpValue = mEditTextLastNameSignUp.getText().toString();
                editTextEmailSignUpValue = mEditTextEmailSignUp.getText().toString();
                editTextPasswordSignUpValue = mEditTextPasswordSignUp.getText().toString();
                editTextConfirmPasswordSignUpValue = mEditTextConfirmPasswordSignUp.getText().toString();
                editTextPhoneNumberSignUpValue = mEditTextPhoneNumberSignUp.getText().toString();
                signUpPost();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginFragment()).commit();
            }
        });
        return view;
    }

    private void signUpPost() {
        if(editTextPasswordSignUpValue.equals(editTextConfirmPasswordSignUpValue)) {

            PostMovie.PostMovies postMovies = new PostMovie.PostMovies(editTextUsernameSignUpValue, editTextFirstNameSignUpValue,
                    editTextLastNameSignUpValue, editTextEmailSignUpValue, editTextPasswordSignUpValue,
                    editTextPhoneNumberSignUpValue, editTextConfirmPasswordSignUpValue);

            Call<SignUpResponse> call = jsonPlaceHolderApi.signUpPost(postMovies);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(@NonNull Call<SignUpResponse> call,@NonNull Response<SignUpResponse> response) {
                    try {
                        if(!response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        SignUpResponse posts = response.body();

                        String content = "";
                        content += "Code: " + response.code() + "\n";

                        Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
                        if(response.code() == 400) {
                            Toast.makeText(getActivity(), "Empty fields!", Toast.LENGTH_LONG).show();
                        }
                        if(response.code() == 404) {
                            Toast.makeText(getActivity(), "An error occurred!", Toast.LENGTH_LONG).show();
                        }
                        if(response.code()!= 404) {
                            mUserId = String.valueOf(posts.getUser_id());
                            getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new OtpFragment(mUserId,
                                    editTextUsernameSignUpValue)).commit();
                            Toast.makeText(getActivity(), posts.getDetails(), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        }
    }
}