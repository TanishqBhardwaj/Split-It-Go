package com.example.SplitItGo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText mEditTextUsernameLogin;
    private EditText mEditTextPasswordLogin;
    private Button mButtonDoneLogin;
    private Button mButtonBackLogin;

    private String editTextUsernameLogin;
    private String editTextPasswordLogin;

    PreferenceUtils pref;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        pref = new PreferenceUtils(getContext());

        mEditTextUsernameLogin = view.findViewById(R.id.editTextUsernameLogin);
        mEditTextPasswordLogin = view.findViewById(R.id.editTextPasswordLogin);
        mButtonDoneLogin = view.findViewById(R.id.buttonDoneLogin);
        mButtonBackLogin = view.findViewById(R.id.buttonBackLogin);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        mButtonDoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextUsernameLogin = mEditTextUsernameLogin.getText().toString();
                editTextPasswordLogin = mEditTextPasswordLogin.getText().toString();
                loginPost();
            }
        });

        mButtonBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginSignUpFragment()).commit();
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
                Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();

                pref.createLoginSession(editTextUsernameLogin, posts.getToken());

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
