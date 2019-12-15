package com.example.SplitItGo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private TextInputLayout editTextUsernameLogin;
    private TextInputLayout editTextPasswordLogin;
    private TextView textViewLoginLink;
    private ImageView imageViewLoginButton;

    private String usernameInput;
    private String passwordInput;
    private String user_id;

    private PreferenceUtils pref;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Context mContext;
    private Activity mActivity;
    private ProgressBar progressBarLogin;
    private View viewBlankLogin;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        pref = new PreferenceUtils(mContext);

        editTextUsernameLogin = view.findViewById(R.id.inputLayoutUsernameLogin);
        editTextPasswordLogin = view.findViewById(R.id.inputLayoutPasswordLogin);
        textViewLoginLink = view.findViewById(R.id.textViewLoginLink);
        imageViewLoginButton = view.findViewById(R.id.imageView2);
        progressBarLogin = view.findViewById(R.id.progressBarLogin);
        viewBlankLogin = view.findViewById(R.id.viewBlankLogin);

        progressBarLogin.setVisibility(View.GONE);
        viewBlankLogin.setVisibility(View.GONE);

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit().create(JsonPlaceHolderApi.class);

        imageViewLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(mContext, HomeActivity.class);
//                startActivity(intent);
//                mActivity.finish();


                if(!validateUsername() | !validatePassword()) {
                    return;
                }
                pref.createLoginSession(usernameInput, "jbjbjbjnjnjn", "1");
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main,
                        new SetPasscodeFragment(usernameInput)).commit();
//                progressBarLogin.setVisibility(View.VISIBLE);
//                viewBlankLogin.setVisibility(View.VISIBLE);
//                mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                loginPost();
            }
        });

        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new SignUpFragment()).commit();
            }
        });

        return view;
    }

    private boolean validateUsername() {
        usernameInput = editTextUsernameLogin.getEditText().getText().toString();
        if(usernameInput.isEmpty()) {
            editTextUsernameLogin.setError("Field can't be empty");
            return false;
        }
        else {
            editTextUsernameLogin.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        passwordInput = editTextPasswordLogin.getEditText().getText().toString();
        if(passwordInput.isEmpty()) {
            editTextPasswordLogin.setError("Field can't be empty");
            return false;
        }
        else {
            editTextPasswordLogin.setError(null);
            return true;
        }
    }

    public void loginPost() {
        PostMovie.PostMovies postMovies = new PostMovie.PostMovies(usernameInput, passwordInput);

        Call<LoginResponse> call = jsonPlaceHolderApi.loginPost(postMovies);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if(response.code() == 400) {
                        Toast.makeText(mContext, "User do not exist!", Toast.LENGTH_LONG).show();
                    }
                    if(response.code() == 404) {
                        Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_LONG).show();
                    }
                    if(!response.isSuccessful()) {
                        return;
                    }
                    LoginResponse posts = response.body();
                    user_id = JWTUtils.decoded(posts.getToken());
                    pref.createLoginSession(usernameInput, posts.getToken(), user_id);

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    progressBarLogin.setVisibility(View.GONE);
                    viewBlankLogin.setVisibility(View.GONE);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivity(intent);
                    mActivity.finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
