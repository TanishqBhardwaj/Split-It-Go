package com.example.SplitItGo;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginSignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button mButtonLogin;
        Button mButtonSignUp;

        View view = inflater.inflate(R.layout.fragment_login_sign_up, container, false);
        mButtonLogin = view.findViewById(R.id.buttonLogin);
        mButtonSignUp = view.findViewById(R.id.buttonSignUp);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginFragment()).commit();
//                Intent intent = new Intent(getContext(), HomeActivity.class);
//                startActivity(intent);
            }
        });

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new SignUpFragment()).commit();
            }
        });
        return view;
    }
}