package com.example.SplitItGo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.SplitItGo.Activity.HomeActivity;
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.LoginResponse;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.Utils.JWTUtils;
import com.example.SplitItGo.Utils.PreferenceUtils;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.google.android.material.textfield.TextInputEditText;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private TextInputEditText mEditTextUsernameLogin;
    private TextInputEditText mEditTextPasswordLogin;
    TextView mTextViewLoginHeading;
    TextView mTextViewLoginLink;
    ImageView imageView;

    private String editTextUsernameLogin;
    private String editTextPasswordLogin;
    String user_id;

    PreferenceUtils pref;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        if(savedInstanceState != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginFragment()).commit();
        }
        pref = new PreferenceUtils(getContext());

        mEditTextUsernameLogin = view.findViewById(R.id.editTextUsernameLogin);
        mEditTextPasswordLogin = view.findViewById(R.id.editTextPasswordLogin);
        mTextViewLoginHeading = view.findViewById(R.id.textViewLoginHeading);
        mTextViewLoginLink = view.findViewById(R.id.textViewLoginLink);
        imageView = view.findViewById(R.id.imageView2);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextUsernameLogin = mEditTextUsernameLogin.getText().toString();
                editTextPasswordLogin = mEditTextPasswordLogin.getText().toString();
                loginPost();
            }
        });

        mTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new SignUpFragment()).commit();
            }
        });
        return view;
    }

    public void loginPost() {
        PostMovie.PostMovies postMovies = new PostMovie.PostMovies(editTextUsernameLogin, editTextPasswordLogin);

        Call<LoginResponse> call = jsonPlaceHolderApi.loginPost(postMovies);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                LoginResponse posts = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Token: " + posts.getToken() + "\n";
                Log.d(posts.getToken(), "onResponse: ");
                try {
                    user_id = JWTUtils.decoded(posts.getToken());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                pref.createLoginSession(editTextUsernameLogin, posts.getToken(), user_id);

                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
