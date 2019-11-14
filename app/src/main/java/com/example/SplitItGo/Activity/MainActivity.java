package com.example.SplitItGo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.SplitItGo.Fragment.LoginSignUpFragment;
import com.example.SplitItGo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame_main, new LoginSignUpFragment()).commit();
    }
}