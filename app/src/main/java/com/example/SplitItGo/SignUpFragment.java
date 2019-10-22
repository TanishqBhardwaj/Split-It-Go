package com.example.SplitItGo;

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

public class SignUpFragment extends Fragment {

    private EditText mEditTextUsernameSignUp;
    private EditText mEditTextFirstNameSignUp;
    private EditText mEditTextLastNameSignUp;
    private EditText mEditTextEmailSignUp;
    private EditText mEditTextPasswordSignUp;
    private EditText mEditTextConfirmPasswordSignUp;
    private EditText mEditTextPhoneNumberSignUp;
    Button mButtonBackSignUp;
    Button mButtonDoneSignUp;

    private String editTextUsernameSignUpValue;
    private String editTextFirstNameSignUpValue;
    private String editTextLastNameSignUpValue;
    private String editTextEmailSignUpValue;
    private String editTextPasswordSignUpValue;
    private String editTextConfirmPasswordSignUpValue;
    private String editTextPhoneNumberSignUpValue;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mEditTextUsernameSignUp = view.findViewById(R.id.editTextUsernameSignUp);
        mEditTextFirstNameSignUp = view.findViewById(R.id.editTextFirstNameSignUp);
        mEditTextLastNameSignUp = view.findViewById(R.id.editTextLastNameSignUp);
        mEditTextEmailSignUp = view.findViewById(R.id.editTextEmailSignUp);
        mEditTextPasswordSignUp = view.findViewById(R.id.editTextPasswordSignUp);
        mEditTextConfirmPasswordSignUp = view.findViewById(R.id.editTextConfirmPasswordSignUp);
        mEditTextPhoneNumberSignUp = view.findViewById(R.id.editTextPhoneNumberSignUp);
        mButtonBackSignUp = view.findViewById(R.id.buttonBackSignUp);
        mButtonDoneSignUp = view.findViewById(R.id.buttonDoneSignUp);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        jsonPlaceHolderApi = RetrofitInstance.getRetrofit(okHttpClient).create(JsonPlaceHolderApi.class);

        mButtonDoneSignUp.setOnClickListener(new View.OnClickListener() {
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

        mButtonBackSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    getFragmentManager().beginTransaction().replace(R.id.fragment_frame, new LoginSignUpFragment()).commit();
            }
        });
        return view;
    }

    public void signUpPost() {
        if(editTextPasswordSignUpValue.equals(editTextConfirmPasswordSignUpValue)) {

            PostMovie.PostMovies postMovies = new PostMovie.PostMovies(editTextUsernameSignUpValue,editTextFirstNameSignUpValue,
                    editTextLastNameSignUpValue, editTextEmailSignUpValue, editTextPasswordSignUpValue, editTextPhoneNumberSignUpValue);

            Call<PostMovie.PostMovies> call = jsonPlaceHolderApi.signUpPost(postMovies);
            call.enqueue(new Callback<PostMovie.PostMovies>() {
                @Override
                public void onResponse(Call<PostMovie.PostMovies> call, Response<PostMovie.PostMovies> response) {

                    if(!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    PostMovie.PostMovies posts = response.body();

                    String content = "";
                    content += "Code: " + response.code() + "\n";
                    content += "Username: " + posts.getUsername() + "\n";
                    Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<PostMovie.PostMovies> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        }
    }

}
