package com.example.SplitItGo.Fragment;

import android.content.Context;
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
import com.example.SplitItGo.Interface.JsonPlaceHolderApi;
import com.example.SplitItGo.Model.PostMovie;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Model.SignUpResponse;
import com.example.SplitItGo.Utils.RetrofitInstance;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private String userId;
    private TextView textViewSignUp;
    private ImageView imageViewSignUpButton;

    private TextInputLayout editTextUsernameSignUp;
    private TextInputLayout editTextEmailSignUp;
    private TextInputLayout editTextPasswordSignUp;
    private TextInputLayout editTextConfirmPasswordSignUp;
    private TextInputLayout editTextPhoneNumberSignUp;

    private String usernameSignUpInput;
    private String emailSignUpInput;
    private String passwordSignUpInput;
    private String confirmPasswordSignUpInput;
    private String phoneNumberSignUpInput;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Context mContext;
    private ProgressBar progressBarLogin;
    private View viewBlankLogin;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        editTextUsernameSignUp = view.findViewById(R.id.inputLayoutUsernameSignUp);
        editTextEmailSignUp = view.findViewById(R.id.inputLayoutEmailSignUp);
        editTextPasswordSignUp = view.findViewById(R.id.inputLayoutPasswordSignUp);
        editTextConfirmPasswordSignUp = view.findViewById(R.id.inputLayoutConfirmPasswordSignUp);
        editTextPhoneNumberSignUp = view.findViewById(R.id.inputLayoutPhoneNumberSignUp);
        textViewSignUp = view.findViewById(R.id.textViewSignUpLink);
        imageViewSignUpButton = view.findViewById(R.id.imageView4);
        progressBarLogin = view.findViewById(R.id.progressBarSignUp);
        viewBlankLogin = view.findViewById(R.id.viewBlankSignUp);

        progressBarLogin.setVisibility(View.GONE);
        viewBlankLogin.setVisibility(View.GONE);

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit().create(JsonPlaceHolderApi.class);

        imageViewSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword()
                        | !validatePhoneNumber()) {
                    return;
                }
                progressBarLogin.setVisibility(View.VISIBLE);
                viewBlankLogin.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                signUpPost();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginFragment()).commit();
            }
        });
        return view;
    }

    private boolean validateUsername() {
        usernameSignUpInput = editTextUsernameSignUp.getEditText().getText().toString().trim();
        if(usernameSignUpInput.isEmpty()) {
            editTextUsernameSignUp.setError("Field can't be empty");
            return false;
        }
        else {
            editTextUsernameSignUp.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        emailSignUpInput = editTextEmailSignUp.getEditText().getText().toString().trim();
        if(emailSignUpInput.isEmpty()) {
            editTextEmailSignUp.setError("Field can't be empty");
            return false;
        }
        else {
            editTextEmailSignUp.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        passwordSignUpInput = editTextPasswordSignUp.getEditText().getText().toString().trim();
        if(passwordSignUpInput.isEmpty()) {
            editTextPasswordSignUp.setError("Field can't be empty");
            return false;
        }
        else {
            editTextPasswordSignUp.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        confirmPasswordSignUpInput = editTextConfirmPasswordSignUp.getEditText().getText().toString().trim();
        if(confirmPasswordSignUpInput.isEmpty()) {
            editTextConfirmPasswordSignUp.setError("Field can't be empty");
            return false;
        }
        else {
            editTextConfirmPasswordSignUp.setError(null);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        phoneNumberSignUpInput = editTextPhoneNumberSignUp.getEditText().getText().toString().trim();
        if(phoneNumberSignUpInput.isEmpty()) {
            editTextPhoneNumberSignUp.setError("Field can't be empty");
            return false;
        }
        else {
            editTextPhoneNumberSignUp.setError(null);
            return true;
        }
    }

    private void signUpPost() {
        if(passwordSignUpInput.equals(confirmPasswordSignUpInput)) {

            PostMovie.PostMovies postMovies = new PostMovie.PostMovies(usernameSignUpInput, emailSignUpInput, passwordSignUpInput,
                    phoneNumberSignUpInput, confirmPasswordSignUpInput);

            Call<SignUpResponse> call = jsonPlaceHolderApi.signUpPost(postMovies);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(@NonNull Call<SignUpResponse> call,@NonNull Response<SignUpResponse> response) {
                    try {
                        if(response.code() == 400) {
                            Toast.makeText(mContext, "Empty fields!", Toast.LENGTH_LONG).show();
                        }
                        if(response.code() == 404) {
                            Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_LONG).show();
                        }
                        if(!response.isSuccessful()) {
                            return;
                        }
                        SignUpResponse posts = response.body();
                        progressBarLogin.setVisibility(View.GONE);
                        viewBlankLogin.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(response.code()!= 404) {
                            userId = String.valueOf(posts.getUser_id());
                            getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new OtpFragment(userId,
                                    usernameSignUpInput)).commit();
                            Toast.makeText(mContext, posts.getDetails(), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(mContext, "An error occurred!", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(mContext, "Passwords do not match.", Toast.LENGTH_LONG).show();
        }
    }
}