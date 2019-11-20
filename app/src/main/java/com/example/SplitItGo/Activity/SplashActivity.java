package com.example.SplitItGo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    PreferenceUtils pref;
    int delay = 0;

    @Override
    protected void onStart() {
        super.onStart();
        delay=1000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new PreferenceUtils(getApplicationContext());

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pref.checkLogin()) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },delay);
    }
}
